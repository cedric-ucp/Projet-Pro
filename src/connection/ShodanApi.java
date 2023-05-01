package connection;

import com.fooock.shodan.ShodanRestApi;
import com.fooock.shodan.model.banner.Banner;
import com.fooock.shodan.model.host.Host;
import io.reactivex.Observable;
import utils.Const;
import utils.Utils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

public class ShodanApi{
    ShodanRestApi api = new ShodanRestApi(Const.SHODAN_API_KEY);
    Map <String , String> bannerData = new HashMap<>();
    Map <String , String> hostData = new HashMap<>();
    public void auditHost(String target){
        try {
            Observable<Host> report = api.hostByIp(false, target);
            for (Host host : report.blockingIterable()) {
                String[] vulnerabilities = host.getVulnerabilities();
                String organization = host.getOrganization();

                if (vulnerabilities != null && vulnerabilities.length > 0)
                    hostData.put("vulnerability", Arrays.toString(vulnerabilities));
                if (organization != null && !organization.isEmpty())
                    hostData.put("organization", organization);

                Iterator<Banner> bannerIterator = host.getBanners().iterator();
                Banner currentBanner = bannerIterator.next();
                while (bannerIterator.hasNext() && currentBanner != null && !bannerDataisBuilt()){
                    buildBannerData(currentBanner);
                    currentBanner = bannerIterator.next();
                }
                System.out.println("hostData : " + hostData.get("vulnerability"));
                System.out.println("\n" + bannerData);
            }
        }
        catch(Exception e){
            Utils.getLogger().log(Level.SEVERE , e.getMessage());   
        }
        startCVEResearch();
    }
    public void buildBannerData(Banner banner){
        if(Utils.valueExists(banner.getTitle()) && !bannerData.containsKey("title"))
            bannerData.put("title" , banner.getTitle());
        if(Utils.valueExists(banner.getIpStr()))
            bannerData.put("ip" , banner.getIpStr());
        if(Utils.valueExists(banner.getVersion()) && !bannerData.containsKey("version"))
            bannerData.put("version" , banner.getVersion());
        if(Utils.valueExists(banner.getDeviceType()) && !bannerData.containsKey("deviceType"))
            bannerData.put("deviceType" , banner.getDeviceType());
        if(Utils.valueExists(banner.getOs()) && !bannerData.containsKey("os"))
            bannerData.put("os" , banner.getOs());
        if(Utils.valueExists(Arrays.toString(banner.getDomains())) && !bannerData.containsKey("domains"))
            bannerData.put("domains" , Arrays.toString(banner.getDomains()));
        if(Utils.valueExists(Arrays.toString(banner.getHostnames())) && !bannerData.containsKey("hostnames"))
            bannerData.put("hostnames" , Arrays.toString(banner.getHostnames()));
        if(Utils.valueExists(banner.getInfo()) && !bannerData.containsKey("info"))
            bannerData.put("info" , banner.getInfo());
        if(Utils.valueExists(banner.getOptions().toString()) && !bannerData.containsKey("options"))
            bannerData.put("options" , banner.getOptions().toString());
        if(Utils.valueExists(banner.getMetadata().toString()) && !bannerData.containsKey("metadata"))
            bannerData.put("metadata" , banner.getMetadata().toString());
        if(Utils.valueExists(String.valueOf(banner.getPort())) && !bannerData.containsKey("port"))
            bannerData.put("port" , String.valueOf(banner.getPort()));
        if(Utils.valueExists(banner.getLocation().toString()) && !bannerData.containsKey("location"))
            bannerData.put("location" , banner.getLocation().toString());
        if(Utils.valueExists(banner.getSslInfo().toString()) && !bannerData.containsKey("sslInfo"))
            bannerData.put("sslInfo" , banner.getSslInfo().toString());
        if(Utils.valueExists(banner.getData()) && !bannerData.containsKey("data"))
            bannerData.put("data" , banner.getData());
    }
    private boolean bannerDataisBuilt(){
        return bannerData.containsKey("title") && bannerData.containsKey("version") && bannerData.containsKey("deviceType") && bannerData.containsKey("os")
                && bannerData.containsKey("domains") && bannerData.containsKey("hostnames") && bannerData.containsKey("info") && bannerData.containsKey("options")
                && bannerData.containsKey("metadata") && bannerData.containsKey("port") && bannerData.containsKey("location") && bannerData.containsKey("sslInfo")
                && bannerData.containsKey("data");
    }

    private void startCVEResearch(){
        CVE cve = new CVE ("https://services.nvd.nist.gov/rest/json/cves/2.0?cveId=" , hostData.get("vulnerability"));
    }
}
