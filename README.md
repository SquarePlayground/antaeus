## Antaeus

Antaeus (/ænˈtiːəs/), in Greek mythology, a giant of Libya, the son of the sea god Poseidon and the Earth goddess Gaia. He compelled all strangers who were passing through the country to wrestle with him. Whenever Antaeus touched the Earth (his mother), his strength was renewed, so that even if thrown to the ground, he was invincible. Heracles, in combat with him, discovered the source of his strength and, lifting him up from Earth, crushed him to death.

Welcome to our challenge.

## The challenge

As most "Software as a Service" (SaaS) companies, Pleo needs to charge a subscription fee every month. Our database contains a few invoices for the different markets in which we operate. Your task is to build the logic that will pay those invoices on the first of the month. While this may seem simple, there is space for some decisions to be taken and you will be expected to justify them.

### Structure
The code given is structured as follows. Feel free however to modify the structure to fit your needs.
```
├── pleo-antaeus-app
|
|       Packages containing the main() application. 
|       This is where all the dependencies are instantiated.
|
├── pleo-antaeus-core
|
|       This is where you will introduce most of your new code.
|       Pay attention to the PaymentProvider and BillingService class.
|
├── pleo-antaeus-data
|
|       Module interfacing with the database. Contains the models, mappings and access layer.
|
├── pleo-antaeus-models
|
|       Definition of models used throughout the application.
|
├── pleo-antaeus-rest
|
|        Entry point for REST API. This is where the routes are defined.
└──
```

## Instructions
Fork this repo with your solution. We want to see your progression through commits (don’t commit the entire solution in 1 step) and don't forget to create a README.md to explain your thought process.

Please let us know how long the challenge takes you. We're not looking for how speedy or lengthy you are. It's just really to give us a clearer idea of what you've produced in the time you decided to take. Feel free to go as big or as small as you want.

Happy hacking 😁!

## Documentation

I had to make a few decisions for the timer and decided upon Coroutines as my method of scheduling.

The primary reason for using Coroutines instead of the usual cron is because I wanted to test out the new feature for myself and this seemed like the perfect opportunity to do so.
Coroutines seem to be a very clean, lightweight thread with great potential.

I decided to create the ScheduleService as a support for the overall BillingService. This distinction to me was very important as I did not want to weigh down one class with unrelated features.

Two of the tests inside BillingServiceTest use a new exception that I have added to the project. “MoneyValueOutOfRangeException” I noticed that the random boolean generator added in the override charge method had a range, so I made the assumption that it should be tested against. 

The supplied logger has been implemented to log out issues with declined payments or other billing issues. In this scenario I would assume that this would be handled by the Billing Department so I have added the InvoiceStatus type of DECLINED.

An enhancement I could make would be to add a rest endpoint so that the billing department could manually go in and update corrected invoices. I did not think this should be part of this particular scope but think it would be an interesting enhancement.

I was tossing up whether I should enable an Elvis operator to throw a Network Error inside the override charge method but ultimately decided against this as it is passing a boolean in the first place. In this scenario I don’t believe throwing it in this case is necessary.

Some of the refactoring that I could do beyond the most recent commit would be general refactoring to reduce reused code or even the implementation of cron jobs. 

The BillingService.chargeInvoices method has not tried to catch the exception InvoiceNotFoundException. The reason I did not implement the individual fetch() of each invoice after getting the fetchAll() method is as this would cause performance degradation.

I could also test for duplicate copies of the same invoice but have implemented .distinct on the billingInvoices to avoid this in the first place. 

A lot of attention has been given to 'when' and 'is' as I wanted to test out new features. These methods could easily have been replaced with if, if else statements but I believe this looks and feels much cleaner.

The use of forEach is also evident which fits into the functional programming style I was trying to achieve. The for method would provide a slight performance increase over forEach but I believe this is unnecessary as it is marginal. Keeping with the style of .filter and .distinct was more important to me.

Overall I'd say that Kotlin is an awesome language to work with and the IntelliJ IDEA is a great IDE to work with.

Total time for this task was: 
4 days of Plural Sight training to learn new Kotlin features and refresh myself on Java/JVM and 4-5 days coding on and off.


## How to run
```
./docker-start.sh
```

## Libraries currently in use
* [Exposed](https://github.com/JetBrains/Exposed) - DSL for type-safe SQL
* [Javalin](https://javalin.io/) - Simple web framework (for REST)
* [kotlin-logging](https://github.com/MicroUtils/kotlin-logging) - Simple logging framework for Kotlin
* [JUnit 5](https://junit.org/junit5/) - Testing framework
* [Mockk](https://mockk.io/) - Mocking library
