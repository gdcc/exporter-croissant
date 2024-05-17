package io.gdcc.spi.export.croissant;

import io.gdcc.spi.export.ExportDataProvider;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import jakarta.json.stream.JsonGenerator;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.skyscreamer.jsonassert.JSONAssert;

public class CroissantExporterTest {

    static CroissantExporter exporter;
    static OutputStream outputStreamMinimal;
    static ExportDataProvider dataProviderMinimal;
    static OutputStream outputStreamMax;
    static ExportDataProvider dataProviderMax;
    static OutputStream outputStreamCars;
    static ExportDataProvider dataProviderCars;

    @BeforeAll
    public static void setUp() {
        exporter = new CroissantExporter();

        outputStreamMinimal = new ByteArrayOutputStream();
        dataProviderMinimal = new ExportDataProvider() {
            @Override
            public JsonObject getDatasetJson() {
                String pathToJsonFile = "src/test/resources/minimal/datasetJson.json";
                try (JsonReader jsonReader = Json.createReader(new FileReader(pathToJsonFile))) {
                    return jsonReader.readObject();
                } catch (FileNotFoundException ex) {
                    return null;
                }
            }

            @Override
            public JsonObject getDatasetORE() {
                String pathToJsonFile = "src/test/resources/minimal/datasetORE.json";
                try (JsonReader jsonReader = Json.createReader(new FileReader(pathToJsonFile))) {
                    return jsonReader.readObject();
                } catch (FileNotFoundException ex) {
                    return null;
                }
            }

            @Override
            public JsonArray getDatasetFileDetails() {
                String pathToJsonFile = "src/test/resources/minimal/datasetFileDetails.json";
                try (JsonReader jsonReader = Json.createReader(new FileReader(pathToJsonFile))) {
                    return jsonReader.readArray();
                } catch (FileNotFoundException ex) {
                    return null;
                }
            }

            @Override
            public JsonObject getDatasetSchemaDotOrg() {
                String pathToJsonFile = "src/test/resources/minimal/datasetSchemaDotOrg.json";
                try (JsonReader jsonReader = Json.createReader(new FileReader(pathToJsonFile))) {
                    return jsonReader.readObject();
                } catch (FileNotFoundException ex) {
                    return null;
                }
            }

            @Override
            public String getDataCiteXml() {
                try {
                    return Files.readString(Paths.get("src/test/resources/minimal/dataCiteXml.xml"), StandardCharsets.UTF_8);
                } catch (IOException ex) {
                    return null;
                }
            }
        };

        outputStreamMax = new ByteArrayOutputStream();
        dataProviderMax = new ExportDataProvider() {
            @Override
            public JsonObject getDatasetJson() {
                String pathToJsonFile = "src/test/resources/max/datasetJson.json";
                try (JsonReader jsonReader = Json.createReader(new FileReader(pathToJsonFile))) {
                    return jsonReader.readObject();
                } catch (FileNotFoundException ex) {
                    return null;
                }
            }

            @Override
            public JsonObject getDatasetORE() {
                String pathToJsonFile = "src/test/resources/max/datasetORE.json";
                try (JsonReader jsonReader = Json.createReader(new FileReader(pathToJsonFile))) {
                    return jsonReader.readObject();
                } catch (FileNotFoundException ex) {
                    return null;
                }
            }

            @Override
            public JsonArray getDatasetFileDetails() {
                String pathToJsonFile = "src/test/resources/max/datasetFileDetails.json";
                try (JsonReader jsonReader = Json.createReader(new FileReader(pathToJsonFile))) {
                    return jsonReader.readArray();
                } catch (FileNotFoundException ex) {
                    return null;
                }
            }

            @Override
            public JsonObject getDatasetSchemaDotOrg() {
                String pathToJsonFile = "src/test/resources/max/datasetSchemaDotOrg.json";
                try (JsonReader jsonReader = Json.createReader(new FileReader(pathToJsonFile))) {
                    return jsonReader.readObject();
                } catch (FileNotFoundException ex) {
                    return null;
                }
            }

            @Override
            public String getDataCiteXml() {
                try {
                    return Files.readString(Paths.get("src/test/resources/max/dataCiteXml.xml"), StandardCharsets.UTF_8);
                } catch (IOException ex) {
                    return null;
                }
            }
        };

        outputStreamCars = new ByteArrayOutputStream();
        dataProviderCars = new ExportDataProvider() {
            @Override
            public JsonObject getDatasetJson() {
                String pathToJsonFile = "src/test/resources/cars/datasetJson.json";
                try (JsonReader jsonReader = Json.createReader(new FileReader(pathToJsonFile))) {
                    return jsonReader.readObject();
                } catch (FileNotFoundException ex) {
                    return null;
                }
            }

            @Override
            public JsonObject getDatasetORE() {
                String pathToJsonFile = "src/test/resources/cars/datasetORE.json";
                try (JsonReader jsonReader = Json.createReader(new FileReader(pathToJsonFile))) {
                    return jsonReader.readObject();
                } catch (FileNotFoundException ex) {
                    return null;
                }
            }

            @Override
            public JsonArray getDatasetFileDetails() {
                String pathToJsonFile = "src/test/resources/cars/datasetFileDetails.json";
                try (JsonReader jsonReader = Json.createReader(new FileReader(pathToJsonFile))) {
                    return jsonReader.readArray();
                } catch (FileNotFoundException ex) {
                    return null;
                }
            }

            @Override
            public JsonObject getDatasetSchemaDotOrg() {
                String pathToJsonFile = "src/test/resources/cars/datasetSchemaDotOrg.json";
                try (JsonReader jsonReader = Json.createReader(new FileReader(pathToJsonFile))) {
                    return jsonReader.readObject();
                } catch (FileNotFoundException ex) {
                    return null;
                }
            }

            @Override
            public String getDataCiteXml() {
                try {
                    return Files.readString(Paths.get("src/test/resources/cars/dataCiteXml.xml"), StandardCharsets.UTF_8);
                } catch (IOException ex) {
                    return null;
                }
            }
        };

    }

    @Test
    public void testGetFormatName() {
        CroissantExporter instance = new CroissantExporter();
        String expResult = "";
        String result = instance.getFormatName();
        assertEquals("croissant", result);
    }

    @Test
    public void testGetDisplayName() {
        assertEquals("Croissant", exporter.getDisplayName(null));
    }

    @Test
    public void testIsHarvestable() {
        assertEquals(false, exporter.isHarvestable());
    }

    @Test
    public void testIsAvailableToUsers() {
        assertEquals(true, exporter.isAvailableToUsers());
    }

    @Test
    public void testGetMediaType() {
        assertEquals("application/json", exporter.getMediaType());
    }

    @Test
    public void testExportDatasetMinimal() throws Exception {
        exporter.exportDataset(dataProviderMinimal, outputStreamMinimal);
        String actual = outputStreamMinimal.toString();
        Files.writeString(Paths.get("src/test/resources/minimal/croissant.json"), prettyPrint(actual), StandardCharsets.UTF_8);
        String expected = Files.readString(Paths.get("src/test/resources/minimal/croissant-expected.json"), StandardCharsets.UTF_8);
        JSONAssert.assertEquals(expected, actual, true);
        assertEquals(prettyPrint(expected), prettyPrint(outputStreamMinimal.toString()));

    }

    @Test
    public void testExportDatasetMax() throws Exception {
        exporter.exportDataset(dataProviderMax, outputStreamMax);
        String actual = outputStreamMax.toString();
        Files.writeString(Paths.get("src/test/resources/max/croissant.json"), prettyPrint(actual), StandardCharsets.UTF_8);
        /*
        First, install pyDataverse from Dans-labs, the "croissant" branch:
        pip3 install --upgrade --no-cache-dir  git+https://github.com/Dans-labs/pyDataverse@croissant#egg=pyDataverse
        You can use this script to export Croissant from a dataset:
        ---
        from pyDataverse.Croissant import Croissant
        #from pyDataverse.Croissant import Croissant
        import json
        #host = "https://dataverse.nl"
        #PID = "doi:10.34894/KMRAYH"
        host = "https://beta.dataverse.org"
        PID = "doi:10.5072/FK2/VQTYHD"
        croissant = Croissant(host, PID)
        print(json.dumps(croissant.get_record(), indent=4, default=str))
        ---
        Finally, uncomment the lines below to check for differences.
         */
//        String pyDataverse = Files.readString(Paths.get("/tmp/pyDataverse.json"), StandardCharsets.UTF_8);
//        JSONAssert.assertEquals(actual, pyDataverse, true);
        String expected = Files.readString(Paths.get("src/test/resources/max/croissant-expected.json"), StandardCharsets.UTF_8);
        JSONAssert.assertEquals(expected, actual, true);
        assertEquals(prettyPrint(expected), prettyPrint(outputStreamMax.toString()));

    }

    @Test
    public void testExportDatasetCars() throws Exception {
        exporter.exportDataset(dataProviderCars, outputStreamCars);
        String actual = outputStreamCars.toString();
        Files.writeString(Paths.get("src/test/resources/cars/croissant.json"), prettyPrint(actual), StandardCharsets.UTF_8);
        String expected = Files.readString(Paths.get("src/test/resources/cars/croissant-expected.json"), StandardCharsets.UTF_8);
        JSONAssert.assertEquals(expected, actual, true);
        assertEquals(prettyPrint(expected), prettyPrint(outputStreamCars.toString()));

    }

    public static String prettyPrint(String jsonObject) {
        try {
            return prettyPrint(getJsonObject(jsonObject));
        } catch (Exception ex) {
            return jsonObject;
        }
    }

    public static String prettyPrint(JsonObject jsonObject) {
        Map<String, Boolean> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory jsonWriterFactory = Json.createWriterFactory(config);
        StringWriter stringWriter = new StringWriter();
        try (JsonWriter jsonWriter = jsonWriterFactory.createWriter(stringWriter)) {
            jsonWriter.writeObject(jsonObject);
        }
        return stringWriter.toString();
    }

    public static JsonObject getJsonObject(String serializedJson) {
        try (StringReader rdr = new StringReader(serializedJson)) {
            try (JsonReader jsonReader = Json.createReader(rdr)) {
                return jsonReader.readObject();
            }
        }
    }

}
