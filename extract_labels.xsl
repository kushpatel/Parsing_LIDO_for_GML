<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:lido="http://www.lido-schema.org">

    <xsl:output method="xml"
            encoding="UTF-8"
            indent="no"
			omit-xml-declaration="yes"/>	
    <xsl:template match="/">
		<xsl:element name="Labels"><xsl:copy-of select="//lido:partOfPlace/lido:namePlaceSet/lido:appellationValue"/></xsl:element>	
    </xsl:template>

</xsl:stylesheet>