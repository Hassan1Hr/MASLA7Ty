package com.hassan.masla7ty.network;

/**
 * Created by Hassan on 5/7/2015.
 */
public class Service {
    private String EnterpriseName;
    private String EnterpriseId;
    private String EnterpriseAddress;
    private String distance;
    private String enterpriseImage;







    public Service(String EnterpriseId,String EnterpriseName,String image, String EnterpriseAddress, String distance)
    {
        this.EnterpriseId = EnterpriseId;
        this.EnterpriseName = EnterpriseName;
        this.EnterpriseAddress = EnterpriseAddress;
        this.enterpriseImage = image;
        this.distance = distance;
       // this.serviceImage = serviceImage;


    }
    public String getEnterpriseImage() {
        return enterpriseImage;
    }

    public void setEnterpriseImage(String enterpriseImage) {
        this.enterpriseImage = enterpriseImage;
    }

    public String getEnterpriseId() {
        return EnterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        EnterpriseId = enterpriseId;
    }

    public String getEnterpriseAddress() {
        return EnterpriseAddress;
    }

    public void setEnterpriseAddress(String enterpriseAddress) {
        EnterpriseAddress = enterpriseAddress;
    }

    public String getEnterpriseName() {
        return EnterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        EnterpriseName = enterpriseName;
    }


    public  String getDistance()
    {
        return distance;
    }
    public void setDistance(String distance)
    {
        this.distance=distance;
    }

}
