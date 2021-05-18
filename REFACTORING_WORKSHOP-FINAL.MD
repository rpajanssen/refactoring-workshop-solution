*Recap*

So a quick recap of some of the things we did to clean up the original class:

- we refactored the class so it now has just one responsibility
  - we extracted a cache class
  - we extracted a file loader (reader) class
  - we extracted an object mapper class
- with this above cleanup we fixed the programming error (BUG) that resulted in a thread
  safety issue  
  
- we generified the cache class and get-methods so we can factor out all ugly class casting code
- we made the get-method have a strongly typed argument which will prevent programming errors and thus
  we prevent runtime errors and production incidents
  
- we generified the mapper class and map-methods so we can factor out all ugly class casting code 
- we factored out using the org.json library since we were also using jackson and there is no reason
  to use both; we finished what previous developers started but abandoned - and left list-container
  class in an unused state - by using the existing unused list-container class icw jackson and on the 
  fly fix java/json property naming convention violations (probably why it did not work before) we have
  been able to simplify the de-serialization code and only use one library for it

- we extracted the mail resource configuration (which is most likely to change in the future) into an 
  enumeration, grouping and binding every part of the configuration (cache-key, json file, target class)
  - we have been able to reduce the duplicated code by about 90% because of the extracted mail resource configuration and
    because of this maintenance of this code has now become cheaper and we are less likely to make a
    programming mistake and are less likely to have production incidents because of this
  - the main class we refactored now complies with the open/close principle : we can add new resources 
    or remove existing resources without having to modify the mail-resource-provider and because of this
    we have speeded up future maintenance and reduced the risk of programming errors for these future
    changes (resulting in less production incidents after future changes)  

- we renamed the class and methods so it is better understandable what they do 
  - we made life easier for developers who now will spend less time understanding the code and can
    be more productive

- we added unit tests for all the classes we added and refactored and achieved a 90% line coverage
  - adding / maintaining these test cost us minimal effort because the classes are setup according to clean coding principles
  - we will detect technical errors build time (locally) instead of after deployment so we will have
    speeded up testing/debugging up to a factor 1000
  - we will have less production incidents now  
  - our code units now have additional behavioural documentation in the form of the unit tests that 
    describe the behavior with readable test method names 

- we have been able to reduce the number of library dependencies
  - with this we reduced the time we need to spend on LCM every sprint
  - with this we reduced the risk of experiencing a security vulnerability we have to address

*Wrap up*

Note that the final commits are all small improvements to the refactored code, that we made during a final code
walk through. Typical things that can popup during a mob or peer review.

Some of the improvements in the final commits have been triggered by the warnings the IDE gives to you. Always pay 
attention to these warnings, they often indicate code smells or programming errors! Often the IDE can perform the 
refactoring for you! Fixing them directly often saves you from having to wade through Sonar findings so you
will speed up delivery and productivity if you pay attention!

Note we got rid of all todos (always do that otherwise they loose meaning and you have lost a powerful tool) but two:
- CCA_PREFERRED_MAIL_TEMPLATE : this template seems to be unused so we need to investigate if we can throw this away
- I don't know what the "BO" prefix stands for and this leads to unclear variable/class/method names, so we need to
  find out what it means and then refactor accordingly.

We should also migrate to a newer JUnit version.

*Future Tips*

- abide by the SOLID coding principles, it'll make your code more testable, more extensible, better re-usable, easier 
  maintainable
- write clean code (read the book Clean Code by Robert C. Martin, his blog: http://cleancoder.com/products)
- extract... extract... extract : the single responsibility principle is the easiest to understand but probably the
  most violated, each class should have one responsibility... each method should have one responsibility... if you
  observe multiple responsibilities... extract it to another method/class... you IDE can do this for you without 
  changing the behavior of your code
- work together, pair program, mob desgin, mob review, ...
- write unit tests... maybe even a bit of TDD... consider your unit tests as a bit of technical documentation
- write code as if you it's a door you need to keep open for the next developer... don't slam it in his/hers face  
