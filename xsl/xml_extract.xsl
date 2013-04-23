<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:lido="http://www.lido-schema.org">

    <xsl:output method="html"
            encoding="UTF-8"
            indent="no"
			omit-xml-declaration="yes"/>	
    <xsl:template match="/">
	<xsl:element name="Record">	
        <!-- make a new root element for the glossary XML -->
        <xsl:element name="EventsWrap">
            <xsl:for-each select="//lido:eventPlace">
				<!-- make an element for the term -->
                <xsl:element name="Event"><xsl:copy-of select="lido:place"/></xsl:element>
                <!-- make an element for the definition -->
                <!--<xsl:element name="definition">Definition: </xsl:element>-->
            </xsl:for-each>
        </xsl:element>
		
		<xsl:element name="SubjectsWrap">
            <xsl:for-each select="//lido:subjectPlace">
                <!-- make an element for the term -->
                <xsl:element name="Subject"><xsl:copy-of select="lido:place"/></xsl:element>
                <!-- make an element for the definition -->
                <!--<xsl:element name="definition">Definition: </xsl:element>-->
            </xsl:for-each>
        </xsl:element>
		
		<xsl:element name="UrlWrap">
            <xsl:for-each select="//lido:recordInfoLink">
                <!-- make an element for the term -->
                <xsl:element name="url"><xsl:copy-of select="."/></xsl:element>
                <!-- make an element for the definition -->
                <!--<xsl:element name="definition">Definition: </xsl:element>-->
            </xsl:for-each>
        </xsl:element>
		
		<xsl:element name="TitleWrap">
            <xsl:for-each select="//lido:titleSet">
                <!-- make an element for the term -->
                <xsl:element name="Title"><xsl:copy-of select="lido:appellationValue"/></xsl:element>
                <!-- make an element for the definition -->
                <!--<xsl:element name="definition">Definition: </xsl:element>-->
            </xsl:for-each>
		</xsl:element>
		
		<xsl:element name="ImagesWrap">
            <xsl:for-each select="//lido:resourceSet">
                <!-- make an element for the term -->
                <xsl:element name="Images"><xsl:copy-of select="lido:resourceRepresentation"/></xsl:element>
                <!-- make an element for the definition -->
                <!--<xsl:element name="definition">Definition: </xsl:element>-->
            </xsl:for-each>
		</xsl:element>
		
		</xsl:element>
    </xsl:template>

</xsl:stylesheet>