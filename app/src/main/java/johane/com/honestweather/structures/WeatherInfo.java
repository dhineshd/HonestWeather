package johane.com.honestweather.structures;

import lombok.Builder;
import lombok.Data;

/**
 * Created by dhinesh.dharman on 6/8/16.
 */

@Builder
@Data
public class WeatherInfo {
    private int temperatureDegreesInF;
}
