package model;

public class CoinMarketCapTicker {

    private String id;
    private String name;
    private String symbol;
    private String rank;
    private String priceUSD;
    private String priceBTC;
    private String VolumeUSD24h;
    private String marketCapUSD;
    private String availableSupply;
    private String totalSupply;
    private String maxSupply;
    private String percentChange1h;
    private String percentChange24h;
    private String percentChange7h;
    private String lastUpdated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPriceUSD() {
        return priceUSD;
    }

    public void setPriceUSD(String priceUSD) {
        this.priceUSD = priceUSD;
    }

    public String getPriceBTC() {
        return priceBTC;
    }

    public void setPriceBTC(String priceBTC) {
        this.priceBTC = priceBTC;
    }

    public String getVolumeUSD24h() {
        return VolumeUSD24h;
    }

    public void setVolumeUSD24h(String volumeUSD24h) {
        VolumeUSD24h = volumeUSD24h;
    }

    public String getMarketCapUSD() {
        return marketCapUSD;
    }

    public void setMarketCapUSD(String marketCapUSD) {
        this.marketCapUSD = marketCapUSD;
    }

    public String getAvailableSupply() {
        return availableSupply;
    }

    public void setAvailableSupply(String availableSupply) {
        this.availableSupply = availableSupply;
    }

    public String getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(String totalSupply) {
        this.totalSupply = totalSupply;
    }

    public String getMaxSupply() {
        return maxSupply;
    }

    public void setMaxSupply(String maxSupply) {
        this.maxSupply = maxSupply;
    }

    public String getPercentChange1h() {
        return percentChange1h;
    }

    public void setPercentChange1h(String percentChange1h) {
        this.percentChange1h = percentChange1h;
    }

    public String getPercentChange24h() {
        return percentChange24h;
    }

    public void setPercentChange24h(String percentChange24h) {
        this.percentChange24h = percentChange24h;
    }

    public String getPercentChange7h() {
        return percentChange7h;
    }

    public void setPercentChange7h(String percentChange7h) {
        this.percentChange7h = percentChange7h;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
