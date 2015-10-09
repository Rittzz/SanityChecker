[![Build Status](https://travis-ci.org/Rittzz/SanityChecker.svg?branch=master)](https://travis-ci.org/Rittzz/SanityChecker)

# SanityChecker
A library to help with checking your model objects for null fields.

## Usage
To use the sanity checker, you must use the *@Sanity* and *@MustExist* annotations in conjunction with the *SanityChecker* class.

### Example
```java
@SanityCheck
class Person {
    @MustExist
    String name;
}

Person person = new Person();
person.name = null;

SanityChecker = new SanityChecker();

// The following will throw a SanityException saying Person.name is null
sanityChecker.check(person);
```

## Special Cases
In addition to normal field handling, the sanity checker will also handle the following cases for you.

- Fields in super classes (if A extends B, when checking A, it will also check fields in B).
- Lists
- Arrays

If you wish to add a special sanity check, you can just have your model object implement the *SanityCheckable* interface which provides a simple "sanityCheck" method which you can use to throw a SanityException yourself.  Take a look at the "Flea" test case for more information.
