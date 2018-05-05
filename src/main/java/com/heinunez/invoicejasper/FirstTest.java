package com.heinunez.invoicejasper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FirstTest {

    private static final String LOGO_PATH = "/close2ulogo.jpg";
    private static final String XML_PATH = "/xml/20549776974-01-FFA1-80.XML";
    private static final String JASPER_PATH = "/jasper/test-factura.jasper";

    public static void main(String... args) throws JRException, ParserConfigurationException, IOException, SAXException {
        //Prepare Data source
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);//enable namespaces !
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document dataSource = db.parse(FirstTest.class.getResourceAsStream(XML_PATH));

        //Set parameters
        File f = new File(FirstTest.class.getResource(LOGO_PATH).getFile());
        byte[] logo_data = Files.readAllBytes(f.toPath());
        Map<String, Object> params = new HashMap<>();
        params.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, dataSource);
        params.put(JRParameter.REPORT_LOCALE, Locale.US);
        params.put("LOGO_DATA", logo_data);

        //Fill report
        JasperPrint jasperPrint = JasperFillManager.fillReport(FirstTest.class.getResourceAsStream(JASPER_PATH), params);

        //Export to pdf
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        OutputStreamExporterOutput output = new SimpleOutputStreamExporterOutput("src/main/resources/result/result.pdf");
        exporter.setExporterOutput(output);
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        exporter.setConfiguration(configuration);
        exporter.exportReport();

        Desktop.getDesktop().open(new File("src/main/resources/result/result.pdf"));
    }

}
