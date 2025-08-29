package com.newpos.store.android.sdk.ability;

import static com.newpos.store.android.sdk.Constant.API_SUCCESS;

import android.text.TextUtils;

import com.newpos.store.android.sdk.base.BaseAbility;
import com.newpos.store.android.sdk.base.BaseApi;
import com.newpos.store.android.sdk.base.BaseException;
import com.newpos.store.android.sdk.base.BaseLog;
import com.newpos.store.android.sdk.base.BaseUtils;
import com.newpos.store.android.sdk.dto.AppElements;
import com.newpos.store.android.sdk.dto.LbsLocationRequest;
import com.newpos.store.android.sdk.dto.LbsLocationResponse;

import java.util.List;
import java.util.Objects;

/**
 * LbsAbility provides access to LBS (Location Based Service) features.
 * <p>
 * It allows requesting location information from the backend service
 * based on cell tower and/or WiFi data.
 * </p>
 */
public class LbsAbility extends BaseAbility {

    /**
     * Constructs a new {@code LbsAbility} instance.
     *
     * @param baseUrl      The base URL for API requests.
     * @param elements     The application elements (e.g. appId, secret, etc.).
     * @param serialNumber The device serial number.
     * @param apis         A list of supported API endpoints.
     *
     * <p>
     * This constructor initializes the base ability through the parent
     * {@link BaseAbility} class and logs all supported API endpoints
     * for debugging purposes.
     * </p>
     */
    public LbsAbility(String baseUrl, AppElements elements, String serialNumber, List<String> apis) {
        super(baseUrl, elements, serialNumber, apis);
        for (String api : apis){
            BaseLog.d("LbsAbility:api:"+api);
        }
    }

    /**
     * Requests location information from the backend service.
     *
     * @param locationRequest The request object containing cell tower / WiFi info.
     * @param wifi            Whether WiFi-assisted location should be enabled.
     * @return A {@link LbsLocationResponse} containing the location result,
     *         or {@code null} if the request failed or the response could not be parsed.
     * @throws BaseException If the backend returns an error code other than {@link API_SUCCESS}.
     *
     * <p><b>Workflow:</b></p>
     * <ol>
     *   <li>Send a request using {@link BaseApi#requestLocation(LbsLocationRequest, boolean)}.</li>
     *   <li>If the response string is empty, return {@code null}.</li>
     *   <li>Parse the JSON response into a {@link LbsLocationResponse} object.</li>
     *   <li>If parsing fails, return {@code null}.</li>
     *   <li>If the response code is not equal to {@link API_SUCCESS}, throw {@link BaseException}.</li>
     *   <li>Otherwise, return the parsed {@link LbsLocationResponse}.</li>
     * </ol>
     *
     * <p><b>Notes:</b></p>
     * <ul>
     *   <li>{@code null} return value indicates request or parsing failure, not necessarily a backend error.</li>
     *   <li>{@link BaseException} indicates that the backend returned an error response.</li>
     * </ul>
     */
    public LbsLocationResponse getLocation(LbsLocationRequest locationRequest, boolean wifi) throws BaseException {
        String response = BaseApi.getInstance().requestLocation(locationRequest, wifi);
        if(TextUtils.isEmpty(response)){
            return null;
        }

        LbsLocationResponse locationResponse = BaseUtils.toObject(response, LbsLocationResponse.class);
        if(locationResponse == null){
            return null;
        }

        if(!Objects.equals(locationResponse.code, API_SUCCESS)){
            throw new BaseException(locationResponse.msg+"["+locationResponse.code+"]");
        }

        return locationResponse;
    }
}
