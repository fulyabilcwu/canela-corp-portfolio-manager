public class Asset {
    private int asset_ID;
    private int portfolio_ID;
    private String assetType;
    private double allocationPercentage;
    private double amount;

    /**
     * 
     * @param asset_ID
     * @param portfolio_ID
     * @param assetType
     * @param allocationPercentage
     * @param amount
     */
    public Asset(
        int asset_ID,
        int portfolio_ID,
        String assetType,
        double allocationPercentage,
        double amount
    ){
        this.asset_ID = asset_ID;
        this.portfolio_ID = portfolio_ID;
        this.assetType = assetType;
        this.allocationPercentage = allocationPercentage;
        this.amount = amount;
    }


    public int getAsset_ID() {
        return asset_ID;
    }

    /**
     * 
     * @param asset_ID
     */
    public void setAsset_ID(int asset_ID) {
        this.asset_ID = asset_ID;
    }


    public int getPortfolio_ID() {
        return portfolio_ID;
    }

    /**
     * 
     * @param portfolio_ID
     */
    public void setPortfolio_ID(int portfolio_ID) {
        this.portfolio_ID = portfolio_ID;
    }

    
    public String getAssetType() {
        return assetType;
    }

    /**
     * 
     * @param assetType
     */
    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public double getAllocationPercentage() {
        return allocationPercentage;
    }


    /**
     * 
     * @param allocationPercentage
     */
    public void setAllocationPercentage(int allocationPercentage) {
        this.allocationPercentage = allocationPercentage;
    }


    public double getAmount() {
        return amount;
    }

    /**
     * 
     * @param amount
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * format the string return 
     */
    public String toString() {
        return String.format(
            "%-12d %-15d %-25s %-18.2f %-15.2f",
            asset_ID,
            portfolio_ID,
            assetType == null ? "" : assetType,
            allocationPercentage,
            amount
        );
    }

 }// end of Asset

