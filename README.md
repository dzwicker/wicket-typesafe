[![Build Status](https://travis-ci.org/dzwicker/wicket-typesafe.png)](https://travis-ci.org/dzwicker/wicket-typesafe)

wicket-typesafe
===============

Wicket typesafe helps to generate wicket-ids in a type safe way. So it helps to solve the refactoring problem when using PropertyModel or CompoundPropertyModel.

### Features
Currently it can be used to construct refactor safe property expressions.

For example, using wicket-typesafe module the following code that depends on strings:

    Person person = new Person();
    IModel<Person> personModel=Model.of(person);
    setDefaultModel(personModel);
    add(new Label("street.name"));

can be replaced with the following:

    Person person = new Person();
    IModel<Person> personModel=Model.of(person);
    setDefaultModel(personModel);
    add(new Label(id(of(Person.class).getStreet().getName())));

although the code is a more verbose then its string alternative it will generate
a compile time error should any properties change instead of failing at runtime
like its more concise string alternative.

The module can generate ids and supports:
* Not final Classes
* Interfaces
* All getter return types even final ones
* of(XXX.class) can be stored in a static way
* id(XXXX) can be stored in a static way


### Installation
Add the following dependencies into your pom.xml

	<dependency>
    <groupId>com.github.dzwicker.wicket</groupId>
    <artifactId>wicket-typesafe</artifactId>
		<version>${typesafe.version}</version>
	</dependency>

### Can I use this? What's the license?
It's only locally-tested code, so take it with a grain of salt for now.

The license is Apache 2.0, so you can do almost anything you like.

### Building MetaGen from source
Just use maven:
    mvn install
    
### Acknowledgements 
The idea for this was lifted wholesale from the [LambdaJ-based proposal on the Wicket wiki](https://cwiki.apache.org/WICKET/working-with-wicket-models.html#WorkingwithWicketmodels-LambdaJ).
Instead of pulling in LambdaJ as a dependency, SafeModel uses jMock's ClassImposteriser directly (both LambdaJ and Mockito use that as well). The code was also simplified to not
require the root object's class to be passed in.
