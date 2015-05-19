package com.pfe.elmokhtar.domotique ;
/**
 * Created by elmokhtar on 3/12/15.
 */
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
public class Utility {
    /**
     * Checks for Null String object
     *
     * @param txt
     * @return true for not null and false for null String object
     */
    public static boolean isNotNull(String txt){
        return txt!=null && txt.trim().length()>0 ? true: false;
    }

    public static int convertToInt(Boolean aBoolean) {
        if (aBoolean)
                return 1;
        else
            return 0;
    }
}