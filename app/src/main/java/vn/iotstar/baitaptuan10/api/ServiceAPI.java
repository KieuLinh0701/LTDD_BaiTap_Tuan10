package vn.iotstar.baitaptuan10.api;

import android.media.Image;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import vn.iotstar.baitaptuan10.constant.Const;
import vn.iotstar.baitaptuan10.model.ImageUpload;

public interface ServiceAPI {
    public static final String BASE_URL = "http://app.iotstar.vn:8081/appfoods/";
    Gson gson = new GsonBuilder().setDateFormat("yyyy MM dd HH:mm:ss").create();

    ServiceAPI serviceAPI = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ServiceAPI.class);

    @Multipart
    @POST("updateimages.php")
    Call<List<ImageUpload>> upload(@Part(Const.MY_ID) RequestBody id,
                                   @Part MultipartBody.Part images);
}
