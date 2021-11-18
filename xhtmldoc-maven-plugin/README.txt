This library is used for Xhtml documentation generation to techsite.

----------------------------------------------

Instructions for manual release of the artifact

Pseudo instructions:
* mvn release:prepare -DdryRun
* move pom.xml.tag pom.xml
* hg ci -m"tagging xhtmldoc-maven-plugin-1.2 release"
* hg tag -m"adding xhtmldoc-maven-plugin-1.2 tag" xhtmldoc-maven-plugin-1.2
* mvn clean source:jar package deploy:deploy
* move pom.xml.next pom.xml
* hg ci -m"preparing xhtmldoc-maven-plugin for next development version"
* hg push -b .

