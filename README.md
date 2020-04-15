# WFM Onboarding Backend Project

The goal of this project is to familiarize you with with our backend development. This document assumes that you will 
have your environment set up. If you do not, please follow the steps in the WFM onboarding document.

## Introduction
This is a small starter project with a company and persona model. We already have a few basic endpoints to CREATE and 
GET both models. In this guide, we will be writing a method to assign a persona to a company, and another method to 
retrieve all personas assigned to a company. 

This is not a comprehensive guide to our architecture. A lot of what you’ll see has been simplified significantly, 
but does closely mimic what we do. 

## Step 1: Clone the base repository

We’ll need to get you a local copy of the code. 

Clone the project to your local dev folder. You can find the clone link on the sidebar.

```
$ git clone ssh://git@ultigit.ultimatesoftware.com:7999/~aaronla/wfm-starter-backend.git
```

We’ll create a new branch for your development. Branches are prefixed with information about the ticket you 
are working on. `feature` is the type of ticket, and the `ULTI-123456` is a reference to the JIRA link. We don’t 
have one for you, so just pretend. 

```
$ git checkout -b feature/ULTI-123456-<YOUR_NAME>
```

## Step 2: Setup and first build

Now we’ll need to set up our environment. First thing is to set up RabbitMQ and MariaDB. Some teams will have you set 
that up on your machine and start them up using `brew`. Today, we’ll leave it to magic and allow docker to do all the 
work for us. 

In the repository folder, run `docker-compose up`. We’ll keep an eye on this window to make sure all our services are 
up and running. If you know what you’re doing, you can add the `-d` tag to run the command as a daemon. Wait a minute
or so for docker to complete it's setup. If you're running as a daemon, you won't get any feedback, but a minute or
so will be sufficient. 

Now that our services are running, we need to set up our database for the first time. We have a script for that 
in `scripts/create_mariadb.sql`. We can pipe that easily into our database using the command: 

```
cat scripts/create_mariadb.sql | mysql -u root -p'Admin1!Admin1!' --protocol=tcp
```

Note that there isn't a space between `-p'Admin1!Admin1!'`. You should not get a prompt to enter your password.

Next, we’ll perform our first package. `mvn clean package`
If that succeeds, we’ll run our project for the first time. `mvn clean spring-boot:run`

## Step 3: Exploring our Project

Let’s walk through what we have here. There’s no coding in this step, but we should become familiar with what we’re 
working with. 

### Persona and Company Domain
In our main project, we’ll see two domains. They’re both pretty similar at this point, just a few differences in their 
model. First thing we’ll look at the controller. When we perform REST calls, this is what will be called. The 
controller will call the service, which will perform whatever business logic we need. Finally, the service will 
talk to the repository, which acts as our interface to the database. 

You’ll notice we also have a Data Transfer Object (DTO). Our models are really small, so they’re mildly unnecessary 
for this project, but we use them in our main projects -- so it’s useful to get familiar with them.

### Resources
Inside the resources folder, we have an `application.properties` file. This is where we put in all the properties 
for the application that’s configured by spring. Here, we also configure spring to automatically create tables in 
our database by looking at our models. We don’t do this in production, since we have very complex tables. In our 
other projects, we use a tool called Liquibase to manipulate our tables. 

## Step 4: Trying it out
Let’s play around with what we have. Open Postman, and create an empty request.

By default, our project is configured to run on port `12345`, which you can configure in the resources file. 
The first thing we’ll try is creating a Persona. Set the request to POST, and use the url `localhost:12345/persona`. 
In the body, use the following JSON. You’ll need to set the body type as JSON as well:

```json
{ 
	"firstName": "Anakin",
	"lastName": "Skywalker"
}
```

Send the request. You should see a `201 Created`, as well as an ID returned to you.

Let’s try to GET that Persona back. Set the request to GET, and use the URL `localhost:12345/persona/{id}`, where ID 
is what was returned to you in the previous step. Send the request, and you should see our Persona returned back. 

Try it out with the Company. Take a look at the Company model to see what it expects, and create one. Make sure you 
can find it again with the GET endpoint. 

## Step 5: Assign Persona endpoint
First thing we’ll do is create a service to do pull a Persona, edit their assigned Company, and then save it. 
Our model already has a Company object, so we won’t need to touch that. 

Let’s work in the PersonaService.java file. We’ll need to configure our CompanyRepository object. We’ll declare it, 
and use constructor injection to initialize it. We're changing a few dependencies in a class, so it's good practice
to look for usages and adjust those as well (Like your test cases).

```java
public class PersonaService {
    
        private PersonaRepository personaRepository;
        private CompanyRepository companyRepository;
    
        public PersonaService(PersonaRepository personaRepository, CompanyRepository companyRepository) {
            this.personaRepository = personaRepository;
            this.companyRepository = companyRepository;
        }
        //...
}
```

Next, let’s set up the method to do the assignment. We’ve decided that we’ll do the assignment from the persona side 
using both the personaId and the companyId we want to assign it to. Our model expects a Company, so we’ll also need 
to pull that as well to send it into the setter. 

```java
public class PersonaService {
    //...
    public Persona assignPersonaToCompany(UUID personaId, UUID companyId) {
        Persona persona = getPersona(personaId).orElseThrow(RuntimeException::new);
        Company company = companyRepository.findById(companyId).orElseThrow(RuntimeException::new);
        persona.setCompany(company);
        return personaRepository.save(persona);
    }
    //...
}
```

A RunTimeException is not the best communicative way to throw an error, as it's not very meaningful. So, let's create
our own. Create a new class in the `shared` folder, called `NotFoundException.java`. We'll make this a simple, generic
error. In our production projects, we'll have common exception classes to use. Make sure to replace the 
`RunTimeException` in the service with our new `NotFoundException`. 

```java
public class NotFoundException extends RuntimeException {
}
```

We’ll now set up the controller. Navigate yourself to PersonaController.java. We’ll be performing a POST request to 
the Persona path, with a request parameter for the companyId. 

```java
public class PersonaController {
    //...
    @PostMapping("/{id}")
    public ResponseEntity<PersonaDto> assignPersonaToCompany(@PathVariable("id") UUID personaId,
                                                             @RequestParam UUID companyId) {
        return ResponseEntity.ok(personaDtoMapper.mapToDto(
                personaService.assignPersonaToCompany(personaId, companyId)));
    }
    //...
}

```

Save, repackage, and re-run the project. 

Go to Postman, and try assigning your Persona to the Company you created in the previous step.

## Step 6: GET all assigned persona of a company
This task requires us to write a query to find all the personas assigned to a company given the companyId. Unlike the 
previous step, this task doesn’t include screenshots of the code you will need to write. However, if you are stuck, 
you can view the solutions in the solutions branch of this project. 

We’ll first head to the CompanyRepository.java, where we’ll write a query to find all the personas with the company. 
Also, take a look at the Persona model. Notice that there is a `@JoinColumn` annotation on the Company. This will make 
your query a lot easier, as it implicitly joins the column to the Company table. 

After writing the query, we’ll need to access it from the service, which is called by the controller. The only path 
variable we will want to use is the company ID. 

When you have finished writing these three methods, try out hitting the endpoints using postman.

## Step 7: Testing
So far, we've created two endpoints to allow assignments and viewing those assignments. It's now good practice to 
ensure our code is functional using unit tests. We can find our tests if we navigate to the `src/test` folder. 

A quick way to run these tests for the first time is to right click the `java` directory, and click on `Run All Tests`.

Navigating into the domain, we can see test cases written for each of our main business logic classes. These examples
are extremely simple, since we do not have any complex business logic associated with it. Our tests generally follow
the "given, when, then" pattern. Simply put, `given` some context to the application, `when` some actions are carried
out, `then` we should expect a set of observable consequences.

There are a handful of tests written for the code provided. Take some time to review how each different kind of test 
is set up. You'll see that our test classes are set up differently depending on what aspect of the project is being
tested. Our starter project has very basic implementation and limited business logic associated with each layer, so
our tests will be fairly straight forward. In our production projects, tests are much more complex as they touch upon 
much more logic in each project. 

We've written a few new methods in step 5 and 6. Before we call our task completed, we should write some test cases for
the methods we have touched upon. Again, solutions are available in the solutions branch. 

## Step 8: Cleanup
Once you are finished with this project, stop your java process from running, and close-down the services (rabbitmq and
mariadb) by running `docker-compose down` in the root of this project. MariaDB is configured with a volume, so your data
will be saved to disk for the next time you want to run `docker-compose up -d` in this project again.

## Conclusion
Congratulations, you have just created two endpoints to allow users to assign a persona to a company, and view the 
assignments that they have created. You will be working with similar frameworks in WFM, so it's good to start with the
basics and get familiar with everything we do! 
