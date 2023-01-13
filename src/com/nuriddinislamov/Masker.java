package com.nuriddinislamov;

import java.util.Map;
import java.util.Objects;
import java.io.FileReader;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Masker {

    public static JSONArray mask(String pathToDataFile, String pathToRulesFile) throws Exception {

        // Create new JSON Parser object to parse JSON from files
        JSONParser jsonParser = new JSONParser();

        // This allows to read data from given files
        FileReader dataFileReader = new FileReader(pathToDataFile);
        FileReader rulesFileReader = new FileReader(pathToRulesFile);


        // Parse JSON and convert it to object
        Object dataObject = jsonParser.parse(dataFileReader);
        Object rulesObject = jsonParser.parse(rulesFileReader);

        // Cast to JSONArray
        JSONArray data = (JSONArray) dataObject;
        JSONArray rules = (JSONArray) rulesObject;

        // Define and read all rules from rules file. Perform operation for every rule
        // split() method here separates two parts of the rule - k or v, and regex pattern rule
        for (Object o : rules) {
            String[] rule = o.toString().split(":");

            String ruleKey = rule[0]; // k or v
            String ruleValue = rule[1]; // value of the rule: serves as KEY to access data value

            // The below 'if-elseif' block of code can be conceptualized and extracted into
            // separate function

            // However, due to time constraints, I have decided to leave as it is now

            // There is a double occurrence of iteration over the same JSON array. This can be avoided,
            // but I left it as it is, because the rules for 'k' and 'v' are different

            if (Objects.equals(ruleKey, "k")) {
                // Iterate over all object in data JSON Array
                for (Object datum : data) {
                    JSONObject jsonObj = (JSONObject) datum;
                    Set keySet = jsonObj.keySet();

                    for (Object key : keySet) {

                        // Perform masking only if key matches the regex pattern in the rules
                        if (key.toString().matches(ruleValue)) {
                            String maskedValue = jsonObj
                                    .get(key)
                                    .toString()
                                    .replaceAll(".",
                                            "*");

                            // Replace value in JSON with new masked value
                            jsonObj.remove(key);
                            jsonObj.put(key, maskedValue);
                        }
                    }
                }

            } else if (Objects.equals(ruleKey, "v")) {
                // Iterate over all data objects in JSON Array
                for (Object datum : data) {
                    JSONObject jsonObj = (JSONObject) datum;

                    // I had to use clone() of the JSON object I was working with, because at this point,
                    // there was an exception type of 'ConcurrentModificationException'

                    // Tbh, I googled, and it worked.

                    Map<String, String> copy = (Map<String, String>) jsonObj.clone();
                    Set keySet = copy.keySet();

                    for (Object key : keySet) {

                        // Determine how many '*' are needed to place in the matched regex
                        int times = 0;
                        Pattern pattern = Pattern.compile(ruleValue);
                        Matcher matcher = pattern.matcher(jsonObj.get(key).toString());

                        if (matcher.find()) {
                            times = matcher.group(0).length();
                        }

                        String maskedValue = jsonObj
                                .get(key)
                                .toString()
                                .replaceAll(ruleValue, "*".repeat(times)); // Replace all matched regex with '*'

                        // Replace value in JSON with new masked value
                        jsonObj.remove(key);
                        jsonObj.put(key, maskedValue);
                    }
                }
            }
        }

        // Finally, return modified data
        return data;
    }
}
