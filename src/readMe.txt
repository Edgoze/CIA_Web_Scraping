
Note on missing URL parts: I have a method called addCiaGovIfNotHaveIt() that adds some parts of the URL that might be
missing from the URL for a country.
Note on European Union: Even though "European Union" is listed as a country in europe, I don't consider it a country
throughout my code, so whenever I see it ignore it through 'continue'
Note on non-available information: If the information is "NA" for something, then depending on the question,
I either ignore the country and continue, or give 0 to the value of the information (for instance, if the population
of a country is "NA", I assign it a value of 0).

========================================================================================================================

ASSUMPTIONS:

Assumptions in getCountries() method
-In essence, what I do in this method is populate a map from country name to its URL.
-I'm assuming that the Country Comparisons URL will be under the card containing the words "Country Comparisons"
-I'm assuming that the listing of countries per Area can be found by clicking the header with text "Area"
in Country Comparisons
-The reason why I visit this page is because a list of all countries is there. This way, I don't need to go
continent by continent to get all the URLs.


Assumptions in countryFlags() method, and in its helper, countryContainsColors()
-I'm assuming that the inputs are colors, in non-capitalized characters
-I'm assuming that the info on the flag of a country is under the "Flag Description" header.


Assumptions in lowestPointInOcean() method
-I'm assuming that the input is an ocean
-I'm assuming that ocean input is formatted exactly as listed on CIA website, i.e., same capitalization, same
spacing
-I'm assuming that I can find the listing of oceans under the section in the home page with header with
text "Oceans"
-I'm assuming that I can find the elevation information under the heading "Elevation" in the Ocean page
-I'm assuming that the elevation information is preceded by the text "lowest point:"
-I'm assuming that the last character in the line that contains the relevant information is "m", which is reasonable
since lowest point information is measured in meters


Assumptions in electricityProduction() method
-I'm assuming that continent input is formatted exactly as listed on CIA website, i.e., same capitalization, same
spacing
-I'm assuming that the continent listing is found under a section called The World & Its Regions in the home page
-I'm assuming that info on electricity production is under the "Electricity - production" header.
-I'm assuming that the info is formatted as first the number of kWh, then info on whether it's a trillion,
or a billion, or a million of them (that's why I get the elements at 0th and 1st index from the String split).


Assumptions in largestCoastlineToLandAreaRatio() method
-Note that if the country is "Saint Helena, Ascension, and Tristan da Cunha", I'm letting its coastline
have the value of 60km
-Note I'm assuming that the "United States Pacific Wildlife Refuges" in Australia and Oceania
don't count as countries since they belong to the U.S., and thus I ignore that "country" page in this
method.
-I'm assuming that the input is a continent
-I'm assuming that continent input is formatted exactly as listed on CIA website, i.e., same capitalization, same
spacing
-I'm assuming all listings under the continent page are countries
-I'm assuming that the continent listing is found under a section called The World & Its Regions in the home page
-I'm assuming that the coastline information is under a heading called "Coastline"
-I'm assuming that the first piece of into under coastline is the value for coastline, which is reasonable
since this seems to be the format in all country pages: first the number, then sometimes a note with curious info.
This is why I grab the 0th element of the split array.
-I'm assuming that the area infromation is under a heading called "Area"
-I'm assuming that the relevant info is between the strings "land:" and "water:", since it's reasonable to think that
the value of the landarea will come in between these two words given the format of the pages. I also assume that the
actual value for land will be the element at index 1 of the split array, since it's reasonable to think that the info
will come after "land:", which is the element at index 0
*I noticed that sometimes there's no "land:" and "water:" breakdown. In that case, I just get the value under "total"


Assumptions in populationAndHighestMeanElevation() method
-I'm assuming that it is always measured in meters
-I'm assuming that the input is a continent
-I'm assuming that continent input is formatted exactly as listed on CIA website, i.e., same capitalization, same
spacing
-I'm assuming that the mean elevation info is on the same line as the "mean elevation:" substring, and that the
actual value of the mean elevation is the word after "elevation:". That is why I get the second element of the
split string
-I'm assuming that population information is under a header with text "Population"


Assumptions in importPartnersThirdLargestIsland() method
-I'm assuming that the input is a sea, like "Caribbean"
-Note that professor Swap allowed me to hard-code the size of Hispaniola, and thus I ignore Dominican Republic
and Haiti in my country in which I go through each country since the size of Hispaniola is already hard-coded
and thus there's no point in going over these two countries.
-Note that France is listed as having overseas islands in the CIA website. Since this is the case,
and France is not an island, I ignore France in the country loop in this method.
-Note that if there's a tie on the size of the third largest island by size, the one of which we'll return the
import partners is the first one we find in the hashmap.
-Note that there was no specification as to how the import partners should be formatted, so I simply return the
p.text() of the p tag under Imports - Partners.


Assumptions in countryInSea() method
-I'm assuming that if the country is an island in this sea, under its "location" header I will find the keywords
"island", and the name of the sea (not necessarily in that order).


Assumptions in landAreaOfThisCountry() method
-Similar assumptions as those for getting area in the largestCoastlineToLandAreaRatio() method.


Assumptions in countriesStartingWithDSortedArea() method
-I assume that the input is a capitalized char
-Other assumptions are similar to those in the largestCoastlineToLandAreaRatio() method.


 Assumptions in containsSubstringOrderedByLandBoundary() method and its helper method, landBoundaryOfThisCountry()
-I'm assuming that info on land boundary is under the "Land boundaries" header in a country's page.
-I'm assuming that the landBoundary information of a country is always in between the string "total:"
  and the string "km" under the "Land Boundaries" header in a country's page.