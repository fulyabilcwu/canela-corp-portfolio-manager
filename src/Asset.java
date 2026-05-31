public class Asset {
    private int asset_ID;
    private int portfolio_ID;
    private String assetName;
    private String assetType;
    private int allocationPercentage;
    private int amount;

    /**
     * 
     * @param asset_ID
     * @param portfolio_ID
     * @param assetName
     * @param assetType
     * @param allocationPercentage
     * @param amount
     */
    public Asset(
        int asset_ID,
        int portfolio_ID,
        String assetName,
        String assetType,
        int allocationPercentage,
        int amount
    ){
        this.asset_ID = asset_ID;
        this.portfolio_ID = portfolio_ID;
        this.assetName = assetName;
        this.assetType = assetType;
        this.allocationPercentage = allocationPercentage;
        this.amount = amount;
    }


    public int getAsset_ID() {
        return asset_ID;
    }


    public void setAsset_ID(int asset_ID) {
        this.asset_ID = asset_ID;
    }


    public int getPortfolio_ID() {
        return portfolio_ID;
    }


    public void setPortfolio_ID(int portfolio_ID) {
        this.portfolio_ID = portfolio_ID;
    }


    public String getAssetName() {
        return assetName;
    }


    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }


    public String getAssetType() {
        return assetType;
    }


    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }


    public int getAllocationPercentage() {
        return allocationPercentage;
    }


    public void setAllocationPercentage(int allocationPercentage) {
        this.allocationPercentage = allocationPercentage;
    }


    public int getAmount() {
        return amount;
    }


    public void setAmount(int amount) {
        this.amount = amount;
    }

 }

