package com.example.gproject.meaning;

public class GetminingHelper {

        public static void getMeaning(String word, Context context, DictionaryCallback callback) {
            setInProgress(true, context);
            new Thread(() -> {
                try {
                    Response<List<WordResult2>> response = RetrofitInstance.getInstance()
                            .create(DictionaryApi.class)
                            .getMeaning(word)
                            .execute();

                    if (response.body() == null) {
                        throw new Exception();
                    }
                    ((Activity) context).runOnUiThread(() -> {
                        setInProgress(false, context);
                        List<WordResult2> results = response.body();
                        if (results != null && !results.isEmpty()) {
                            callback.onSuccess(results.get(0));
                        }
                    });
                } catch (Exception e) {
                    ((Activity) context).runOnUiThread(() -> {
                        setInProgress(false, context);
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        callback.onError();
                    });
                }
            }).start();
        }

        private static void setInProgress(boolean inProgress, Context context) {
            Activity activity = (Activity) context;
            if (activity != null) {
                activity.runOnUiThread(() -> {
                    View searchBtn = activity.findViewById(R.id.searchBtn);
                    ProgressBar progressBar = activity.findViewById(R.id.progressBar);

                    if (searchBtn != null && progressBar != null) {
                        if (inProgress) {
                            searchBtn.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.VISIBLE);
                        } else {
                            searchBtn.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        }

        public interface DictionaryCallback {
            void onSuccess(WordResult2 result);

            void onError();
        }
    }

}
