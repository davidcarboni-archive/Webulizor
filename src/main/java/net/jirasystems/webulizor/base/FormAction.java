package net.jirasystems.webulizor.base;

import java.io.IOException;

import net.jirasystems.webulizor.helpers.Link;
import net.jirasystems.webulizor.interfaces.Action;
import net.jirasystems.webulizor.interfaces.Post;

/**
 * Extends {@link ViewAction}, adding a "submitTo" property to the Velocity
 * context.
 * 
 * @author David Carboni
 * 
 */
public class FormAction extends ViewAction {

	private Class<? extends Post> submitTo;

	/**
	 * @param submitTo
	 *            The Action this form will submit to.
	 */
	public FormAction(Class<? extends Post> submitTo) {
		this.submitTo = submitTo;
	}

	/**
	 * Default constructor.
	 */
	public FormAction() {
		// Default constructor.
	}

	@Override
	public Action perform() throws IOException {

		put("submitTo",
				new Link(this.getServletContext(), this.getRequest())
						.resolve(submitTo));
		return super.perform();
	}

	/**
	 * @return the submitTo
	 */
	public Class<? extends Post> getSubmitTo() {
		return submitTo;
	}

	/**
	 * @param submitTo
	 *            the submitTo to set
	 */
	public void setSubmitTo(Class<? extends Post> submitTo) {
		this.submitTo = submitTo;
	}

}
