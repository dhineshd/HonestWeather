package johane.com.honestweather.structures;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * OpenWeatherMap response object
 *
 * Created by dhinesh.dharman on 6/9/16.
 */
@Data
public class OWMWeatherResponse {
    private Map<String, Double> coord;
    private List<Map<String, String>> weather;
    private Map<String, Double> main;
    private Map<String, Double> wind;
    private Map<String, Double> rain;
    private Map<String, Double> clouds;
    private long dt;
    private String name;
}
