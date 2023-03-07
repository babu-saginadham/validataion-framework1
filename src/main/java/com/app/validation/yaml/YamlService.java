package com.app.validation.yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlService {

    private static final String CONFIG_FILE_PATH = "/path/to/config.yaml";

    public void updateConfig(String moduleName, Map<String, Object> newAttributes) throws IOException {
        Yaml yaml = createYamlInstance();
        Map<String, Object> config = yaml.load(YamlService.class.getResourceAsStream(CONFIG_FILE_PATH));

        if (config.containsKey(moduleName)) {
            Map<String, Object> module = (Map<String, Object>) config.get(moduleName);
            module.putAll(newAttributes);

            
            
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);
            options.setIndent(2);
            
            Representer representer = new Representer(options	);
            representer.addClassTag(module.getClass(), Tag.MAP);
            
            Yaml updatedYaml = new Yaml(representer, options);

            try (FileWriter writer = new FileWriter(CONFIG_FILE_PATH)) {
                updatedYaml.dump(config, writer);
            }
        } else {
            throw new RuntimeException("Module not found in config: " + moduleName);
        }
    }

    private Yaml createYamlInstance() {
       
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);
        
        Representer representer = new Representer(options);
        representer.addClassTag(Map.class, Tag.MAP);
        
        return new Yaml(representer, options);
    }
    
    private void updateConfig(String attribute, String type, boolean required, ValidationRule validationRule) throws IOException {
        // Load the YAML configuration from the file
        Map<String, Object> config = loadConfig();

        // Create a new map for the attribute's validation rules
        Map<String, Object> attributeRules = new LinkedHashMap<>();

        // Add the "type" and "required" properties to the attribute rules
        attributeRules.put("type", type);
        attributeRules.put("required", required);

        // If the attribute has a validation rule, add it to the attribute rules map
        if (validationRule != null) {
            Map<String, Object> validationMap = new LinkedHashMap<>();
            if (validationRule.getMin() != null) {
                validationMap.put("min", validationRule.getMin());
            }
            if (validationRule.getMax() != null) {
                validationMap.put("max", validationRule.getMax());
            }
            if (validationRule.getRegex() != null) {
                validationMap.put("regex", validationRule.getRegex());
            }
            if (validationRule.getMessage() != null) {
                validationMap.put("message", validationRule.getMessage());
            }
            attributeRules.put("validation", validationMap);
        }

        // Add the attribute's rules to the configuration map
        config.put(attribute, attributeRules);

        // Save the updated YAML configuration to the file
        saveConfig(config);
    }

    private Map<String, Object> loadConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File configFile = new File("config.yml");
        if (!configFile.exists()) {
            return new LinkedHashMap<>();
        }
        return mapper.readValue(configFile, new TypeReference<LinkedHashMap<String, Object>>() {});
    }

    private void saveConfig(Map<String, Object> config) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File configFile = new File("config.yml");
        mapper.writeValue(configFile, config);
    }


}

