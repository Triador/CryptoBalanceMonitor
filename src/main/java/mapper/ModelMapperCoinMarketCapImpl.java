package mapper;

import com.google.gson.JsonObject;
import mapper.ModelCoinMarketCapMapper;
import model.CoinMarketCapTicker;

public class ModelMapperCoinMarketCapImpl implements ModelCoinMarketCapMapper {

    public CoinMarketCapTicker mapToCoinMarketCapTicker(JsonObject jsonObject) {

        CoinMarketCapTicker coinMarketCapTicker = new CoinMarketCapTicker();
        coinMarketCapTicker.setId(jsonObject.get("id").getAsString());
        coinMarketCapTicker.setName(jsonObject.get("name").getAsString());
        coinMarketCapTicker.setSymbol(jsonObject.get("symbol").getAsString());
        coinMarketCapTicker.setRank(jsonObject.get("rank").getAsString());
        coinMarketCapTicker.setPriceUSD(jsonObject.get("price_usd").getAsString());
        coinMarketCapTicker.setPriceBTC(jsonObject.get("price_btc").getAsString());
        coinMarketCapTicker.setVolumeUSD24h(jsonObject.get("24h_volume_usd").getAsString());
        coinMarketCapTicker.setMarketCapUSD(jsonObject.get("market_cap_usd").getAsString());
        coinMarketCapTicker.setAvailableSupply(jsonObject.get("available_supply").getAsString());
        coinMarketCapTicker.setTotalSupply(jsonObject.get("total_supply").getAsString());
        coinMarketCapTicker.setMaxSupply(jsonObject.get("max_supply").getAsString());
        coinMarketCapTicker.setPercentChange1h(jsonObject.get("percent_change_1h").getAsString());
        coinMarketCapTicker.setPercentChange24h(jsonObject.get("percent_change_24h").getAsString());
        coinMarketCapTicker.setPercentChange7h(jsonObject.get("percent_change_7d").getAsString());
        coinMarketCapTicker.setLastUpdated(jsonObject.get("last_updated").getAsString());

        return coinMarketCapTicker;
    }
}
