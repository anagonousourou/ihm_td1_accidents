package ihm.accidents.activities;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public interface UpdateTypes {

    void updateTypesList(String types);

    default List<String> extractTypes(String jsonReps) throws JSONException {
        JSONArray array=new JSONArray(jsonReps);
        List<String> types=new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            types.add(array.getJSONObject(i).getString("label"));
        }
        return types;
    }

}
