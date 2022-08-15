# Web Scraping on CIA website

## Description

Implemented a web scraping program on the CIA factbook website (https://www.cia.gov/the-world-factbook/) as the third project for my networks and social 
systems class.

It's able to answer the following questions:

1. List all countries with flags containing both *red* and *green* colors.
2. What is the lowest point in the *Atlantic Ocean*?
3. Find the largest country in *Africa* in terms of Electricity Production.
4. Norway is notoriously known for having the largest coastline in Europe despite its small land
area. Which country in *Europe* has the largest coastline to land area ratio?
5. What is the population of the country in *South America* with the highest mean elevation?
6. Many islands rely on imports from larger countries. Which countries are the Imports Partners
for the third largest island (by total area) in the *Caribbean*?
7. Provide a list of all countries starting with the letter *“D”*, sorted by total area, smallest to largest.
8. Provide a list of all countries that contain the substring "United" in their names, sorted by land boundaries

All the italicized words are replaceable. For instance, in the first question, we could replace read and green with blue and white.
Note however that the programs makes certain simple assumptions, one of them being that the formatting of the italicized words
will be followed when providing input. For instance, "red" and "green" are lowercase, so any color input is expected to be lowercase.
In question 3, on the other hand, the input continent is expected to have the first letter capitalized, like "Africa".

*Note*: some of the questions might not produce answers anymore. This is not due to an error in the program, but because the website is frequently 
update, and some of these updates break the assumptions of the program. At the time of this writing, among others, question 1 works correctly,
so it's a good one to run for a demo of the program. Also, note that the program doesn't always immediately produce an answer due to all the pages it needs
to explore in order to answer some questions, so you might need to wait a bit to see the output.

## Project overview

There are only two classes in the project: CiaParserMain, which is the main file and the one that handles user input, and CiaParser, which contains
the bulk of the code in the program. In it, I use JSoup to explore a page in order to find the information needed to answer each question. 

Each method in this class is named in such a way that it makes it easy to figure it out its purpose (for instance, the methods that answer question 1 are called
getCountries(), countryFlags(), and the main method, countryContainsColors() ). The methods are ordered based on the ordering of the questions:
the topmost one is for question 1, the bottommost one for question 8.

I have a file called readMe.txt where I mention the assumptions made in each method, giving it a read might be useful to better understand
the workings of each function.

## Design evaluation / Things to improve

I think I should have organized the project using a class for each question rather than having a single file with many methods, each of which
answers a different question. Even though they are organized from top to bottom to match the order of the questions, organizing the project
by classes would probably be easier to read.

In addition, I think I should have used more helper functions in order to break down long methods. Even though my variable naming
is intuitive and the code is organized, having too much code in one method can make it hard to read and understand.

## Installation

To run the project, clone the repository and run the CiaParserMain class in the src directory. You will be prompted to provide input in the terminal.

## External Resources

The link to the World Factbook Website by the CIA is this: https://www.cia.gov/the-world-factbook/
