// package net.jirasystems.webulizor.base;
//
// import java.io.IOException;
// import java.util.Map;
//
// import net.jirasystems.webulizor.helpers.Link;
// import net.jirasystems.webulizor.interfaces.Action;
// import net.jirasystems.webulizor.interfaces.Post;
//
// public class MultiAction extends AbstractAction {
//
// private RedirectAction redirectAction;
// private ViewAction viewAction;
// private FormAction formAction;
//
// // private DatabaseAction databaseAction;
//
// @Override
// public Action perform() throws IOException {
//
// // Redirect takes precedence:
// if (redirectAction != null && redirectAction.getLocation() != null) {
// redirectAction.perform();
// } else {
//
// // Render the template:
// if (viewAction != null) {
// viewAction.perform();
// }
//
// // Set the form action:
// if (formAction != null) {
// formAction.perform();
// }
// }
//
// // Subclasses should return an Action if necessary:
// return null;
// }
//
// /**
// * @return the submitTo
// */
// public Class<? extends Post> getSubmitTo() {
// Class<? extends Post> result = null;
//
// if (formAction != null) {
// result = formAction.getSubmitTo();
// }
//
// return result;
// }
//
// /**
// * @param submitTo
// * the submitTo to set
// */
// public void setSubmitTo(Class<? extends Post> submitTo) {
// if (submitTo == null) {
// formAction = null;
// } else if (formAction == null) {
// formAction = new FormAction(submitTo);
// } else {
// formAction.setSubmitTo(submitTo);
// }
// }
//
// /**
// * @return the templatePath
// */
// public String getTemplatePath() {
// String result = null;
//
// if (viewAction != null) {
// result = viewAction.getTemplatePath();
// }
//
// return result;
// }
//
// /**
// * @param templatePath
// * the templatePath to set
// */
// public void setTemplatePath(String templatePath) {
// if (templatePath == null) {
// viewAction = null;
// } else if (viewAction == null) {
// viewAction = new ViewAction(templatePath);
// } else {
// viewAction.setTemplatePath(templatePath);
// }
// }
//
// public void put(String name, Object value) {
// getViewAction().put(name, value);
// }
//
// private Map<String, Object> getData() {
// return getViewAction().getData();
// }
//
// /**
// * @return A lazily-instantiated {@link Link} instance.
// */
// public Link getLink() {
// return getViewAction().getLink();
// }
//
// /**
// * @return the redirectAction
// */
// public ViewAction getRedirectAction() {
// return redirectAction;
// }
//
// /**
// * @return A lazily-instantiated {@link ViewAction} instance.
// */
// public ViewAction getViewAction() {
// if (viewAction == null) {
// viewAction = new ViewAction();
// }
// return viewAction;
// }
//
// /**
// * @return A lazily-instantiated {@link FormAction} instance.
// */
// public FormAction getFormAction() {
// if (formAction == null) {
// formAction = new FormAction();
// }
// return formAction;
// }
//
// /**
// * @return A lazily-instantiated {@link DatabaseAction} instance.
// */
// private DatabaseAction getDatabaseAction() {
// if (databaseAction == null) {
// databaseAction = new DatabaseAction();
// }
// return databaseAction;
// }
//
//}
