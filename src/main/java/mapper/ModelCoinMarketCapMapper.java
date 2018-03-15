package mapper;

import com.google.gson.JsonObject;
import model.CoinMarketCapTicker;

public interface ModelCoinMarketCapMapper {
    CoinMarketCapTicker mapToCoinMarketCapTicker(JsonObject jsonObject);
}
