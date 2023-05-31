package utils;

import org.apache.http.HttpStatus;
import outputs.HandleDisplayForUser;

public class Const {
    public static String REQUEST_ERROR_MESSAGE = "Api connection error";
    public static String REQUEST_SUCCESS_MESSAGE = "Request successfully";

    public static String REQUEST_FAILED_MESSAGE = "Request failed";
    public static String REQUEST_BAD_INFORMATION = "Bad information set";
    public static String REQUEST_TIMEOUT = "Request timeout";
    public static String REQUEST_UNKNOWN_STATUS = "Unknown status code";
    public static int STATUS_OK = HttpStatus.SC_OK;
    public static int STATUS_CREATED = HttpStatus.SC_CREATED;
    public static int STATUS_ACCEPTED = HttpStatus.SC_ACCEPTED;
    public static int STATUS_FAILED = HttpStatus.SC_EXPECTATION_FAILED;
    public static int STATUS_UNAUTHORIZED = HttpStatus.SC_UNAUTHORIZED;
    public static int STATUS_TIMEOUT = HttpStatus.SC_REQUEST_TIMEOUT;
    public static int STATUS_BAD_INFORMATION = HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION;
    public static int STATUS_TOO_MANY_REQUEST = 429;
    public static String START_SCAN = "START_SCAN";
    public static String SCAN_INFO = "SCAN_INFO";
    public static String SCAN_STATUS = "SCAN_STATUS";
    public static String SCAN_RESULTS = "SCAN_RESULTS";
    public static final String START_SCAN_URL = "https://api.nmap.online/v01/start_scan";
    public static final String SCAN_INFO_URL = "https://api.nmap.online/v01/scan_info";
    public static final String SCAN_STATUS_URL = "https://api.nmap.online/v01/check_scan_status";
    public static final String SCAN_RESULTS_URL = "https://api.nmap.online/v01/scan_result";
    public static final String AUDIT_ACTION = "audit";
    public static final String SINGLE_SCAN = "nmap_command";
    public static final String TARGET = "target";
    public static final String SCAN_TYPE = "scan_type";
    public static final String SCAN_ID = "scan_id";
    public static final String RESULT = "result";
    public static final String STATUS_CODE = "status_code";
    public static final String SCAN_PORT = "SCAN PORT";
    public static final String OS_INFO_SCAN = "OS INFO";
    public static final String AGGRESSIVE_SCAN = "AGGRESSIVE SCAN";
    public static final String MALWARE_DETECTION_SCAN = "MALWARE DETECTION";
    public static final String SERVICE_DETECTION_SCAN = "SERVICE DETECTION";
    public static final String FIREWALLING_SCAN = "FIREWALLING SCAN";
    public static String NMAP_API_KEY_VALUE = "dkjhj9iacgm63abk3bbpdzrgap7ie3b2zgikl9bxfsekmmjg";
    public static String NMAP_API_KEY_KEY = "NMAP-API-KEY";
    public static String NMAP_CONTENT_TYPE_KEY = "content-type";
    public static String NMAP_CONTENT_TYPE_VALUE = "multipart/form-data";
    public static String SHODAN_API_KEY = "pSeIs6iC4leTsGGENTS5DEBx6yoo0C6e";
    public static Boolean STOP_LOADING_BAR = false;
    public static HandleDisplayForUser display = new HandleDisplayForUser();
}
