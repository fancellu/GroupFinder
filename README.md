
## GroupFinder

A simple piece of utility code to take a List and group its elements based on List membership, 
whilst returning unmatching elements, without unnecessary changing of order, via Group case class

This code is quite useful for processing shopping carts, looking for X for Y deals, or A+B+C deals

[![Build Status](https://travis-ci.org/fancellu/GroupFinder.svg?branch=master)](https://travis-ci.org/fancellu/GroupFinder)

`case class Groups[A](matched: List[List[A]], unmatched: List[A])`

Run `sbt test` to execute the unit tests

### Usage

e.g.

Looking for 1+2+3 in any order
 
`GroupFinder.findGroups(List(2, 1, 3, 1, 4, 2, 1, 2, 3), List(1,2,3)) shouldBe
           Groups(List(List(2, 1, 3), List(1, 2, 3)), List(4, 1, 2))`
 
Looking for 2+1+1 in any order 
 
`GroupFinder.findGroups(List(1, 2, 1, 2, 1, 1, 2), List(2, 1, 1)) shouldBe 
    Groups(List(List(1, 2, 1), List(2, 1, 1)), List(2))`
    
Looking for buy one, get one free on bananas    

`case class Product(name: String, price: Double)
         findGroups(List(Product("banana",10),Product("banana",10),Product("apple",5)), List.fill(2)(Product("banana",10))) shouldBe         
           Groups(List(List(Product("banana",10),Product("banana",10))), List(Product("apple",5)))`    
  
Note, the matched lists are in the order they were found in, as are the unmatched elements
The code is tail recursive, immutable, and does its best to be fairly efficient

