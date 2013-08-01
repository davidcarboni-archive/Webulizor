
Webulizor
----------


### What is it?

Webulizor is a Java webapp micro-framework. It's built on two ideas: first, that in a world of continuous delivery the best place to put most configuration is directly in the code and second that refactoring code shouldn't break web page links. It's about not having to keep things in synch across code, configuration and HTML.


### The basics

Webulizor assumes that URLs with a file extension are static content and URLs without a file extension are controllers. It uses a servlet filter to dispatch requests to either the default servlet or the application servlet. Using a filter means there's no `/servlet/` in your URLs, so everything can be top-level.

Controllers are defined using annotations such as `@Route` and `@HomeAction`. `@Route` allows you to specify the URI for a controller and `@HomeAction` indicates the default controller. A few base classes are provided to get you started: `AbstractAction`, `RedirectAction` and `ViewAction`. By default you get Apache Velocity for templating. `ViewAction` defaults to looking for a `.html` resource in the same package structure as your controller, or you're free to define a different resource path. The default database is HSQLDB, which can be changed. Controllers that use a database connection are transactional by default but can be marked as non-transactional where necessary.


### Getting started

The quickest way to get started is:
 * add Webulizor to your project as a WAR overlay
 * reference the -classes JAR in your dependencies
 * create a class that extends `ViewAction` and annotate it with `@Route` and `@HomeAction`
 * create a Velocity template to match the package structure and name of your class


