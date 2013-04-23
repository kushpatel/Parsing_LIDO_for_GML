<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:lido="http://www.lido-schema.org">

	<xsl:param name="objectid" as="xs:integer" required="yes"/>

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

	<!-- template for the document element -->
	<xsl:template match="record">
  		<xsl:apply-templates select="node()" />
	</xsl:template>
	<xsl:template match="metadata">
  		<xsl:apply-templates select="node()" />
	</xsl:template>

	<xsl:template match="header"/>
	<xsl:template match="lido:resourceRepresentation[@lido:type='thumb' or @lido:type='large' or @lido:type='list']"/>

	<xsl:template match="lido:resourceSet">
  		<lido:resourceSet>
     			<lido:resourceRepresentation lido:type="original">
        			<lido:linkResource lido:formatResource="jpg"><xsl:sequence select="$objectid"/>.jpg</lido:linkResource>
     			</lido:resourceRepresentation>
     			<xsl:apply-templates select="@* | *"/>
  		</lido:resourceSet>
	</xsl:template>


</xsl:stylesheet>
