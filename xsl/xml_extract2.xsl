<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:lido="http://www.lido-schema.org">

    <xsl:output method="xml" 
            encoding="UTF-8"
            indent="no"/>
	
    <xsl:template match="/">
	<Places>
		 <xsl:copy-of select="/record/metadata/lido:lido/lido:descriptiveMetadata/lido:objectRelationWrap/lido:subjectWrap/lido:subjectSet/lido:subject/lido:subjectPlace/node()">
         </xsl:copy-of>
	</Places>
    </xsl:template>
	
	<!--<xsl:template match="/">
		<Places>
			<xsl:copy-of select="/record/metadata/lido:lido/lido:descriptiveMetadata/lido:objectRelationWrap/lido:subjectWrap/lido:subjectSet/lido:subject/lido:subjectPlace/lido:place/node()">
			</xsl:copy-of>
		</Places>
    </xsl:template>-->
</xsl:stylesheet>