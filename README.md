# DnData #

DnData is a data management web application intended to be a platform for providing and managing the data of tabletop roleplaying games in a structured, computer-friendly format.

## The Problem ## 

Tabletop roleplaying games (TTRPGs) like Pathfinder and Dungeons and Dragons contain a lot of data. This data consists of large collections of spells, monsters, feats and more. By default, this data is published in the source books of each game. In some cases, this information is also made available on reference sites online.

Although the online reference sites allow some basic search functionality, by and large this data remains highly unstructured both online and offline. Anyone seeking to create their own app or website using such data either has to perform a lot of manual data entry or scrape existing websites. Manual data entry may be the only option if the intended product requires a structure that cannot be inferred from scraped data, while scraping is not a long term viable method for maintaining the data collection of an app or website. Because of this, many projects are never completed or even attempted due to the impractical amount of work in creating or maintaining its source data. DnData aims to change all this.

## The Solution ##

DnData is a platform that uses JSON Schema definitions to create highly granular data structures for objects (spells, monsters, etc) in a TTRPG. The platform also has extensive support for content entry, change management and content review, to facilitate that the data is entered correctly and easily into its data structure. The data collections created this way can then easily be published as versioned zipped JSON files. DnData provides an REST API endpoint that can be queried to find out what data is available, and if applicable automatically download updated data files and import the structured data from them. In this way, not only does DnData allow developers to avoid having to do manual data entry or scraping, but they can even set up their software to automatically check for data updates and import changes to their own system if necessary. The JSON schemas that define the data structures are also available through DnData, allowing anyone to create data model classes matching our data structures.

Obviously, DnData relies on data entry to maintain its own data collection, which is done by volunteers. DnData is provided for free, and is open source.

## Current state of DnData ##

As of this writing, while DnData is designed to be game-agnostic (meaning it can be used for any game system), data structures have only been defined for Pathfinder 2nd Edition, from publisher Paizo Inc. You can see the currently available content on the status endpoint: https://dndata-production.herokuapp.com/status/PF2.

## More information ##

For more information check out our github wiki!

## Copyright acknowledgement
Pathfinder is a registered trademark of Paizo Inc. Pathfinder Second Edition and the Pathfinder Second Edition Compatibility Logo are trademarks of Paizo Inc. The Pathfinder-Icons font is © 2019 Paizo Inc. These trademarks and copyrighted works are used under the Pathfinder Second Edition Compatibility License. See paizo.com/pathfinder/compatibility for more information on this license.

