package net.jirasystems.webulizor.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserJourney {

	private static Map<String, List<Interaction>> interactions = new ConcurrentHashMap<String, List<Interaction>>();

	public static void addRequest(HttpServletRequest request) {

		HttpSession session = request.getSession(true);
		String id = session.getId();

		// Use double-checked locking for smoothest running:
		List<Interaction> list;
		Map<String, List<Interaction>> interactions = UserJourney.interactions;
		if (!interactions.containsKey(id)) {

			synchronized (interactions) {
				list = interactions.get(id);
				if (list == null) {
					list = new ArrayList<Interaction>();
					interactions.put(id, list);
				}
			}

		} else {

			list = interactions.get(id);

		}

		synchronized (list) {
			list.add(new Interaction(request));
		}
	}

	public static Map<String, List<Interaction>> getInteractions() {
		Map<String, List<Interaction>> interactions = UserJourney.interactions;
		UserJourney.interactions = new ConcurrentHashMap<String, List<Interaction>>();
		return interactions;
	}
}
