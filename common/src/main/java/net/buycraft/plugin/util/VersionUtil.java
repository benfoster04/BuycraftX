package net.buycraft.plugin.util;

import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;
import net.buycraft.plugin.data.responses.Version;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Arrays;

@UtilityClass
public class VersionUtil {
    public static Version getVersion(OkHttpClient client, String platform) throws IOException {
        Request request = new Request.Builder()
                .url("https://plugin.buycraft.net/versions/" + platform)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            return null;
        }

        try (ResponseBody body = response.body()) {
            return new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create()
                    .fromJson(body.string(), Version.class);
        }
    }

    public static boolean isVersionGreater(String one, String two) {
        String[] componentsOne = one.split("\\.");
        String[] componentsTwo = two.split("\\.");

        int verLen = Math.max(componentsOne.length, componentsTwo.length);

        int[] numOne = new int[verLen];
        int[] numTwo = new int[verLen];

        for (int i = 0; i < componentsOne.length; i++) {
            numOne[i] = Integer.parseInt(componentsOne[i]);
        }
        for (int i = 0; i < componentsTwo.length; i++) {
            numTwo[i] = Integer.parseInt(componentsTwo[i]);
        }

        // Quick exclusion check.
        if (Arrays.equals(numOne, numTwo)) {
            return false;
        }

        for (int i = 0; i < numOne.length; i++) {
            if (numTwo[i] == numOne[i])
                continue;

            if (numTwo[i] > numOne[i])
                return true;
        }

        return false;
    }
}
