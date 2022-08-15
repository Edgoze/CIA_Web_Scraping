import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.*;


public class CiaParser {
    private final String home = "https://www.cia.gov/the-world-factbook";
    private Document currentDoc;
    Map<String, String> countryMap;

    /*
     * Constructor that initializes the base URL and loads
     * the document produced from that URL
     */
    public CiaParser() {
        try {
            this.currentDoc = Jsoup.connect(this.home).get();
            getCountries();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String addCiaGovIfNotHaveIt(String theUrl) {
        if (!theUrl.contains("cia.gov")) {
            theUrl = "cia.gov" + theUrl;
        }
        if (!theUrl.contains("https://www.")) {
            theUrl = "https://www." + theUrl;
        }
        return theUrl;
    }

    /*
     * Creates article map to be a mapping of article titles to url from our current doc
     */
    public void getCountries() {
        Elements cards = this.currentDoc.getElementsByClass("card-exposed");
        Element countryComparisonCard = null;
        for (Element card : cards) {
            String cardText = card.text();
            if (cardText.contains("Country Comparisons")) {
                countryComparisonCard = card;
            }
        }
        Elements a = countryComparisonCard.getElementsByClass("card-exposed__link");
        String articleURL = a.attr("href");
        String completeArticleUrl = addCiaGovIfNotHaveIt(articleURL);
        try {
            this.currentDoc = Jsoup.connect(completeArticleUrl).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Elements hrefHeaders = this.currentDoc.getElementsByClass("mb30");
        Element areaHeader = null;
        for (Element header : hrefHeaders) {
            String headerText = header.text();
            if (headerText.equals("Area")) {
                areaHeader = header;
            }
        }
        Elements areaHeaderATag = areaHeader.getElementsByClass("link-button");
        String areaUrl = areaHeaderATag.attr("href");
        String completeAreaUrl = addCiaGovIfNotHaveIt(areaUrl);
        try {
            this.currentDoc = Jsoup.connect(completeAreaUrl).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Elements countryATags = this.currentDoc.getElementsByClass("content-table-link");
        countryMap = new HashMap<>();
        for (Element aTag : countryATags) {
            countryMap.put(aTag.text(), addCiaGovIfNotHaveIt(aTag.attr("href")));
        }
        Set mySet = countryMap.keySet();
        for (Object country : mySet) {
            String countryInString = (String) country;
        }

        try {
            this.currentDoc = Jsoup.connect(this.home).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public Set<String> countryFlags(String colorOne, String colorTwo) {
        Set<String> output = new TreeSet<>();
        for (String country : countryMap.keySet()) {
            try {
                this.currentDoc =
                        Jsoup.connect(countryMap.get(country)).get();
                if (countryContainsColors(colorOne, colorTwo)) {
                    output.add(country);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return output;
    }

    private boolean countryContainsColors(String colorOne, String colorTwo) {
        Elements informationHeader = this.currentDoc.getElementsByClass("mt30");
        Element flagDescriptionHeader = null;
        for (Element header : informationHeader) {
            if (header.text().equals("Flag description")) {
                flagDescriptionHeader = header;
                Element divParentOfFlagDescrition = flagDescriptionHeader.parent();
                Elements pTag = divParentOfFlagDescrition.getElementsByTag("p");
                String pTagText = pTag.text();
                int potentialStartIndexOfNoteText = pTagText.indexOf("note:");
                if (potentialStartIndexOfNoteText != -1) {
                    pTagText = pTagText.substring(0, potentialStartIndexOfNoteText);
                }
                if ((pTagText.contains(" " + colorOne + " ")
                        || pTagText.contains(" " + colorOne + ",")
                        || pTagText.contains(" " + colorOne + ".")
                        || pTagText.contains(" " + colorOne + ";")
                        || pTagText.contains(" " + colorOne + ":"))
                    &&
                (pTagText.contains(" " + colorTwo + " ")
            || pTagText.contains(" " + colorTwo + ",")
            || pTagText.contains(" " + colorTwo + ".")
                        || pTagText.contains(" " + colorTwo + ";")
                || pTagText.contains(" " + colorTwo + ":"))
                ) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public String lowestPointInOcean(String ocean) {
        String lowestPoint = "";
        Elements sections = this.currentDoc.getElementsByClass("content-card-teaser");
        Element oceanHeader = null;
        for (Element section : sections) {
            if (section.text().contains("Ocean")) {
                oceanHeader = section;
            }
        }
        Elements aTagsWorld = oceanHeader.getElementsByTag("a");
        Element targetOcean = null;
        for (Element aTag : aTagsWorld) {
            if (aTag.text().equals(ocean)) {
                targetOcean = aTag;
            }
        }
        String oceanHref = targetOcean.attr("href");
        try {
            this.currentDoc = Jsoup.connect(addCiaGovIfNotHaveIt(oceanHref)).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Elements sectionsInsideOceanPage = this.currentDoc.getElementsByClass("mt30");
        Element elevationHeader = null;
        for (Element section : sectionsInsideOceanPage) {
            if (section.text().equals("Elevation")) {
                elevationHeader = section;
                Element divParentOfLocationDescrition = elevationHeader.parent();
                Elements pTag = divParentOfLocationDescrition.getElementsByTag("p");
                String elevInfo = pTag.text();
                int indexWhereMeanElevInfoStarts = elevInfo.indexOf("lowest point:");
                int indexWhereMeanElevInfoEnds = elevInfo.indexOf("m") + 1;
//              Sometimes the index is -1, this happens when there's no elevation info
                if (indexWhereMeanElevInfoStarts == -1) {
                    continue;
                }
                lowestPoint = elevInfo.substring(
                        indexWhereMeanElevInfoStarts, indexWhereMeanElevInfoEnds);
            }
        }
        return lowestPoint;
    }

    public String electricityProduction(String continent) {
        String countryWithLargestProduction = "";
        double greatestProduction = 0;
        Elements sections = this.currentDoc.getElementsByClass("content-card-teaser");
        Element worldAndRegionsHeader = null;
        for (Element section : sections) {
            if (section.text().contains("The World & Its Regions")) {
                worldAndRegionsHeader = section;
            }
        }
        Elements aTagsWorld = worldAndRegionsHeader.getElementsByTag("a");
        Element continentaTag = null;
        for (Element aTag : aTagsWorld) {
            if (aTag.text().equals(continent)) {
                continentaTag = aTag;
            }
        }
        String continentHref = continentaTag.attr("href");
        try {
            this.currentDoc = Jsoup.connect(addCiaGovIfNotHaveIt(continentHref)).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Elements aTags = this.currentDoc.getElementsByClass("link-button bold");
        HashMap<String, String> continentCountryMap = new HashMap<>();
        for (Element aTag : aTags) {
            continentCountryMap.put(aTag.text(), addCiaGovIfNotHaveIt(aTag.attr("href")));
        }
        for (String country : continentCountryMap.keySet()) {
            if (country.equals("European Union")) {
                continue;
            }
            try {
                this.currentDoc = Jsoup.connect(continentCountryMap.get(country)).get();
                Elements informationHeader = this.currentDoc.getElementsByClass("mt30");
                Element locationHeader = null;
                for (Element header : informationHeader) {
                    if (header.text().equals("Electricity - production")) {
                        locationHeader = header;
                        Element divParentOfLocationDescrition = locationHeader.parent();
                        Elements pTag = divParentOfLocationDescrition.getElementsByTag("p");
                        String elecInfo = pTag.text();
                        String[] brokenDownInfo = elecInfo.split(" ");
                        String elecProductionInString = brokenDownInfo[0].replaceAll(",", "");
                        if (elecProductionInString.equals("NA")) {
                            continue;
                        }
                        double elecProduction = Double.parseDouble(elecProductionInString);
                        String unitOfProduction = brokenDownInfo[1];
                        if (unitOfProduction.equals("trillion")) {
                            elecProduction = elecProduction * Math.pow(10, 12);
                        } else if (unitOfProduction.equals("billion")) {
                            elecProduction = elecProduction * Math.pow(10, 9);
                        }  else if (unitOfProduction.equals("million")) {
                            elecProduction = elecProduction * Math.pow(10, 6);
                        }
                        if (elecProduction > greatestProduction) {
                            greatestProduction = elecProduction;
                            countryWithLargestProduction = country;
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return countryWithLargestProduction;
    }

    public String largestCoastlineToLandAreaRatio(String continent) {
        String countryWithLargest = "";
        double greatestRatio = 0;
        Elements sections = this.currentDoc.getElementsByClass("content-card-teaser");
        Element worldAndRegionsHeader = null;
        for (Element section : sections) {
            if (section.text().contains("The World & Its Regions")) {
                worldAndRegionsHeader = section;
            }
        }
        Elements aTagsWorld = worldAndRegionsHeader.getElementsByTag("a");
        Element continentaTag = null;
        for (Element aTag : aTagsWorld) {
            if (aTag.text().equals(continent)) {
                continentaTag = aTag;
            }
        }
        String continentHref = continentaTag.attr("href");
        try {
            this.currentDoc = Jsoup.connect(addCiaGovIfNotHaveIt(continentHref)).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Elements aTags = this.currentDoc.getElementsByClass("link-button bold");
        HashMap<String, String> continentCountryMap = new HashMap<>();
        for (Element aTag : aTags) {
            continentCountryMap.put(aTag.text(), addCiaGovIfNotHaveIt(aTag.attr("href")));
        }
        for (String country : continentCountryMap.keySet()) {
            if (country.equals("United States Pacific Island Wildlife Refuges")) {
                continue;
            } else if (country.equals("European Union")) {
                continue;
            }
            double coastlineNumber = 0;
            double landAreNumber = 0;
            double ratio = 0;
            try {
                this.currentDoc = Jsoup.connect(continentCountryMap.get(country)).get();
                Elements informationHeader = this.currentDoc.getElementsByClass("mt30");
                Element coastLineHeader = null;
                Element areaHeader = null;
                for (Element header : informationHeader) {
                    if (header.text().equals("Area")) {
                        areaHeader = header;
                        Element divParentOfLocationDescrition = areaHeader.parent();
                        Elements pTag = divParentOfLocationDescrition.getElementsByTag("p");
                        String areaInfo = pTag.text();
                        int indexWhereMeanElevInfoStarts = 0;
                        int indexWhereMeanElevInfoEnds = 0;
                        if (areaInfo.contains("land:") && areaInfo.contains("water")) {
                            indexWhereMeanElevInfoStarts = areaInfo.indexOf("land:");
                            indexWhereMeanElevInfoEnds = areaInfo.indexOf("water") + 1;
                        } else {
                            indexWhereMeanElevInfoStarts = areaInfo.indexOf("total:");
                            indexWhereMeanElevInfoEnds = areaInfo.indexOf("km") + 1;
                        }

                        if (indexWhereMeanElevInfoStarts == -1) {
                            continue;
                        }
                        String landAreaInfoString = areaInfo.substring(
                                indexWhereMeanElevInfoStarts, indexWhereMeanElevInfoEnds);
                        String[] landAreaInfoStringBrokenDow = landAreaInfoString.split(" ");
                        String landAreaString = landAreaInfoStringBrokenDow[1];
                        if (landAreaString.equals("NA")) {
                            continue;
                        }
                        landAreaString = landAreaString.replaceAll(",", "");
                        landAreNumber = Double.parseDouble(landAreaString);
                    }
                    if (header.text().equals("Coastline")) {
                        coastLineHeader = header;
                        Element divParentOfLocationDescrition = coastLineHeader.parent();
                        Elements pTag = divParentOfLocationDescrition.getElementsByTag("p");
                        String coastInfo = pTag.text();
                        String[] brokenDownInfo = coastInfo.split(" ");
                        String coastlineString = brokenDownInfo[0];
                        coastlineString = coastlineString.replaceAll(",", "");
                        if (coastlineString.equals("NA")) {
                            continue;
                        } else if (country.equals("Saint Helena, Ascension, and Tristan da Cunha")) {
                            coastlineString = "60";
                        }
                        coastlineNumber = Double.parseDouble(coastlineString);
                    }
                    ratio = coastlineNumber / landAreNumber;
                    if (ratio > greatestRatio) {
                        greatestRatio = ratio;
                        countryWithLargest = country;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return countryWithLargest;
    }

    public String populationAndHighestMeanElevation(String continent) {
        String countryWithGreatestMeanElevation = "";
        double greatestMeanElevation = 0;
        String populationOfTheCountryWithGreatestMeanElevation = "";
        Elements sections = this.currentDoc.getElementsByClass("content-card-teaser");
        Element worldAndRegionsHeader = null;
        for (Element section : sections) {
            if (section.text().contains("The World & Its Regions")) {
                worldAndRegionsHeader = section;
            }
        }
        Elements aTagsWorld = worldAndRegionsHeader.getElementsByTag("a");
        Element continentaTag = null;
        for (Element aTag : aTagsWorld) {
            if (aTag.text().equals(continent)) {
                continentaTag = aTag;
            }
        }
        String continentHref = continentaTag.attr("href");
        try {
            this.currentDoc = Jsoup.connect(addCiaGovIfNotHaveIt(continentHref)).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Elements aTags = this.currentDoc.getElementsByClass("link-button bold");
        HashMap<String, String> continentCountryMap = new HashMap<>();
        for (Element aTag : aTags) {
            continentCountryMap.put(aTag.text(), addCiaGovIfNotHaveIt(aTag.attr("href")));
        }
        for (String country : continentCountryMap.keySet()) {
            if (country.equals("European Union")) {
                continue;
            }
            try {
                this.currentDoc = Jsoup.connect(continentCountryMap.get(country)).get();
                Elements informationHeader = this.currentDoc.getElementsByClass("mt30");
                Element locationHeader = null;
                for (Element header : informationHeader) {
                    if (header.text().equals("Elevation")) {
                        locationHeader = header;
                        Element divParentOfLocationDescrition = locationHeader.parent();
                        Elements pTag = divParentOfLocationDescrition.getElementsByTag("p");
                        String elevInfo = pTag.text();
                        int indexWhereMeanElevInfoStarts = elevInfo.indexOf("mean elevation:");
//                      Sometimes the index is -1, this happens there's no elevation info
                        if (indexWhereMeanElevInfoStarts == -1) {
                            continue;
                        }
                        String relevantInfo = elevInfo.substring(indexWhereMeanElevInfoStarts);
                        String[] brokenDownInfo = relevantInfo.split(" ");
                        String meanElevationInString = brokenDownInfo[2];
                        if (meanElevationInString.equals("NA")) {
                            continue;
                        }
                        meanElevationInString = meanElevationInString.replaceAll(",", "");
                        double meanElevation = Double.parseDouble(meanElevationInString);
                        if (meanElevation > greatestMeanElevation) {
                            greatestMeanElevation = meanElevation;
                            countryWithGreatestMeanElevation = country;
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            this.currentDoc = Jsoup.connect(continentCountryMap.get(countryWithGreatestMeanElevation)).get();
            Elements informationHeader = this.currentDoc.getElementsByClass("mt30");
            Element populationHeader = null;
            for (Element header : informationHeader) {
                if (header.text().equals("Population")) {
                    populationHeader = header;
                    Element divParentOfLocationDescrition = populationHeader.parent();
                    Elements pTag = divParentOfLocationDescrition.getElementsByTag("p");
                    String[] brokenDownInfo = pTag.text().split(" ");
                    populationOfTheCountryWithGreatestMeanElevation = brokenDownInfo[0];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "The country is " + countryWithGreatestMeanElevation + " " +
                "and its population is " + populationOfTheCountryWithGreatestMeanElevation;
    }

    public String importPartnersThirdLargestIsland(String sea) {
//      Not Integer to country bc, though unlikely, two countries might have same landArea
        HashMap<String, Double> sizeToName = new HashMap<>();
        sizeToName.put("Hispaniola", 76420.0);

        for (String country : countryMap.keySet()) {
            if (country.equals("France")) {
                continue;
            }
            try {
                this.currentDoc =
                        Jsoup.connect(countryMap.get(country)).get();
                if (country.equals("Dominican Republic") || country.equals("Haiti")) {
                    continue;
                }
                if (countryInSea(sea)) {
                    sizeToName.put(country, landAreaOfThisCountry());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Set<String> countryNames = sizeToName.keySet();
//      If we have less than three countries, we return null
        if (countryNames.size() < 3) {
            return null;
        }
        ArrayList<Double> toSortValuesOfLandArea = new ArrayList<>();
        for (String name : countryNames) {
            toSortValuesOfLandArea.add(sizeToName.get(name));
        }
        toSortValuesOfLandArea.sort(null);
        double thirdLargestLandArea = toSortValuesOfLandArea.get(toSortValuesOfLandArea.size() - 3);
        Set<String> findCountryThatHasThisArea = sizeToName.keySet();
        String countryWithThirdLargestArea = "";
        for (String candidate : findCountryThatHasThisArea) {
            if (sizeToName.get(candidate) == thirdLargestLandArea) {
                countryWithThirdLargestArea = candidate;
                break;
            }
        }

        if (countryWithThirdLargestArea.equals("Hispaniola")) {
            return "The island is Hispaniola and its partners are " +
                    importPartnersOfTarget("Dominican Republic") + " and " +
                    importPartnersOfTarget("Haiti");
        }

        return "The country is " + countryWithThirdLargestArea + " and its partners are: " +
                importPartnersOfTarget(countryWithThirdLargestArea);
    }


    private boolean countryInSea(String sea) {
        Elements informationHeader = this.currentDoc.getElementsByClass("mt30");
        Element locationHeader = null;
        for (Element header : informationHeader) {
            if (header.text().equals("Location")) {
                locationHeader = header;
                Element divParentOfLocationDescription = locationHeader.parent();
                Elements pTag = divParentOfLocationDescription.getElementsByTag("p");
                if ((
                        pTag.text().contains(" island ")
                        || pTag.text().contains("island,")
                        || pTag.text().contains("island."))
                        &&
                        ((pTag.text().contains(sea) ||
                                pTag.text().contains(" " + sea + " ")
                                || pTag.text().contains(sea + ",")
                                || pTag.text().contains(sea + "."))
                )) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private double landAreaOfThisCountry() {
        Elements informationHeader = this.currentDoc.getElementsByClass("mt30");
        Element areaHeader = null;
        double landAreaNumber = 0;
        for (Element header : informationHeader) {
            if (header.text().equals("Area")) {
                areaHeader = header;
                Element divParentOfLocationDescrition = areaHeader.parent();
                Elements pTag = divParentOfLocationDescrition.getElementsByTag("p");
                String areaInfo = pTag.text();
                int indexWhereMeanElevInfoStarts = areaInfo.indexOf("total:");
                int indexWhereMeanElevInfoEnds = areaInfo.indexOf("km") + 1;;
                if (indexWhereMeanElevInfoStarts == -1) {
                    continue;
                }
                String landAreaInfoString = areaInfo.substring(
                        indexWhereMeanElevInfoStarts, indexWhereMeanElevInfoEnds);
                String[] landAreaInfoStringBrokenDow = landAreaInfoString.split(" ");
                String landAreaString = landAreaInfoStringBrokenDow[1];
                if (landAreaString.equals("NA")) {
                    return 0;
                }
                landAreaString = landAreaString.replaceAll(",", "");
                landAreaNumber = Double.parseDouble(landAreaString);
            }
        }
        return landAreaNumber;
    }

    private String importPartnersOfTarget(String country) {
        try {
            this.currentDoc =
                    Jsoup.connect(countryMap.get(country)).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Elements informationHeader = this.currentDoc.getElementsByClass("mt30");
        Element importsHeader = null;
        String partners = "";
        for (Element header : informationHeader) {
            if (header.text().equals("Imports - partners")) {
                importsHeader = header;
                Element divParentOfImportsInfo = importsHeader.parent();
                Elements pTag = divParentOfImportsInfo.getElementsByTag("p");
                partners = pTag.text();
            }
        }
        return partners;
    }

    public LinkedList<String> countriesStartingWithSomeCharSortedArea(char x) {
        Set<String> countriesStartingWithX = new TreeSet<>();
        for (String country : countryMap.keySet()) {
            if (country.charAt(0) == x) {
                countriesStartingWithX.add(country);
            }
        }

        HashMap<String, Double> countryToArea = new HashMap<>();
        for (String country : countriesStartingWithX) {
            if (country.equals("European Union")) {
                continue;
            }
            try {
                this.currentDoc = Jsoup.connect(countryMap.get(country)).get();
                countryToArea.put(country, landAreaOfThisCountry());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ArrayList<Double> toSortAreas = new ArrayList<>();
        for (String country : countriesStartingWithX) {
            toSortAreas.add(countryToArea.get(country));
        }
        toSortAreas.sort(null);

        LinkedList<String> output = new LinkedList<>();
        for (Double area : toSortAreas) {
            for (String country : countriesStartingWithX) {
                if (countryToArea.get(country) == area) {
                    output.add(country);
                }
            }
        }
        return output;
    }

    public LinkedList<String> containsSubstringOrderedByLandBoundary(String substring) {
        Set<String> countriesWithSubstring = new TreeSet<>();
        for (String country : countryMap.keySet()) {
            if (country.contains(substring)) {
                countriesWithSubstring.add(country);
            }
        }

        HashMap<String, Double> countryToLandBoundary = new HashMap<>();
        for (String country : countriesWithSubstring) {
            if (country.equals("European Union")) {
                continue;
            }
            try {
                this.currentDoc = Jsoup.connect(countryMap.get(country)).get();
                countryToLandBoundary.put(country, landBoundaryOfThisCountry());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ArrayList<Double> toSortAreas = new ArrayList<>();
        for (String country : countriesWithSubstring) {
            toSortAreas.add(countryToLandBoundary.get(country));
        }
        toSortAreas.sort(null);

        LinkedList<String> output = new LinkedList<>();
        for (Double area : toSortAreas) {
            for (String country : countriesWithSubstring) {
                if (countryToLandBoundary.get(country) == area) {
                    output.add(country);
                }
            }
        }
        return output;
    }

    private Double landBoundaryOfThisCountry() {
        Elements informationHeader = this.currentDoc.getElementsByClass("mt30");
        Element landBoundaryHeader = null;
        double landBoundaryNumber = 0;
        for (Element header : informationHeader) {
            if (header.text().equals("Land boundaries")) {
                landBoundaryHeader = header;
                Element divParentOfHeader = landBoundaryHeader.parent();
                Elements pTag = divParentOfHeader.getElementsByTag("p");
                String landInfo = pTag.text();
                int indexWhereLandInfoStarts = landInfo.indexOf("total:");
                int indexWhereLandInfoEnds = landInfo.indexOf("km") + 1;;
                if (indexWhereLandInfoStarts == -1) {
                    continue;
                }
                String landBoundaryRelevantInfoToString = landInfo.substring(
                        indexWhereLandInfoStarts, indexWhereLandInfoEnds);
                String[] landBoundaryToStringRelevantInfoBrokenDown = landBoundaryRelevantInfoToString.split(" ");
                String landBoundary = landBoundaryToStringRelevantInfoBrokenDown[1];
                if (landBoundary.equals("NA")) {
                    return 0.0;
                }
                landBoundary = landBoundary.replaceAll(",", "");
                landBoundaryNumber = Double.parseDouble(landBoundary);
            }
        }
        return landBoundaryNumber;
    }
}
