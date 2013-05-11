// package net.jirasystems.webulizor.helpers;
//
// import java.net.URI;
// import java.net.URISyntaxException;
// import java.util.Arrays;
//
// import javax.servlet.http.HttpServletRequest;
//
// import net.jirasystems.webulizor.framework.AppException;
// import net.jirasystems.webulizor.interfaces.Action;
//
// import org.apache.http.client.utils.URIBuilder;
//
// public class Links {
//
// private final Action action;
//
// public Links(Action action) {
// this.action = action;
// }
//
// public URI path(Class<? extends Action> destination,
// QueryString... queryString) {
//
// String path = absolutePath(Path.getPath(destination));
// try {
// URIBuilder uriBuilder = new URIBuilder(path);
// if (queryString.length > 0) {
// uriBuilder.setQuery(queryString[0].toQueryString());
// }
// return uriBuilder.build();
// } catch (URISyntaxException e) {
// throw new RuntimeException("Error building "
// + URI.class.getSimpleName() + " for path " + path
// + " and query string " + Arrays.toString(queryString), e);
// }
// }
//
// public URI url(Class<? extends Action> destination,
// QueryString... queryString) {
// return url().resolve(path(destination, queryString));
// }
//
// /**
// * @return The base URL of the application, which is the scheme, host, port
// * (if applicable) and context path (if any).
// */
// public URI url() {
//
// // Gather information:
// HttpServletRequest request = action.getRequest();
// int serverPort = request.getServerPort();
// boolean secure = request.isSecure();
//
// // URI components:
// String scheme = request.getScheme();
// String host = request.getServerName();
// int port;
// if ((!secure && serverPort == 80) || (secure && serverPort == 443)) {
// port = -1;
// } else {
// port = serverPort;
// }
// String userInfo = null;
// String path = action.getServletContext().getContextPath();
// String query = null;
// String fragment = null;
//
// // Build URI:
// try {
// return new URI(scheme, userInfo, host, port, path, query, fragment);
// } catch (URISyntaxException e) {
// throw new AppException("Error building URL: scheme=" + scheme
// + ", userInfo=" + userInfo + ", host=" + host + ", port="
// + port + ", path=" + path + ", query=" + query
// + ", fragment=" + fragment, e);
// }
// }
//
// /**
// * Adds the application context path (if any) to the given path.
// *
// * @param path
// * The path to absolutizze.
// * @return context+[/]path
// */
// public String absolutePath(String path) {
// return action.getServletContext().getContextPath() + leadingSlash(path);
// }
//
// /**
// * Ensures the given path starts with a '/'.
// *
// * @param path
// * The path to be checked and, if necessary, modified.
// * @return If the given path starts with a slash, the given path, otherwise
// * a the path with a leading slash.
// */
// private String leadingSlash(String path) {
// if (path.startsWith("/")) {
// return path;
// } else {
// return "/" + path;
// }
// }
//}
