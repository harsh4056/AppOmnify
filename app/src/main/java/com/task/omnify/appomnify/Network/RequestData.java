package com.task.omnify.appomnify.Network;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.task.omnify.appomnify.Interfaces.ArticleResponseListener;
import com.task.omnify.appomnify.Interfaces.CommentResponeListener;
import com.task.omnify.appomnify.Interfaces.TopStoriesListListener;
import com.task.omnify.appomnify.Models.Article;
import com.task.omnify.appomnify.Models.Comment;
import com.task.omnify.appomnify.Utils.AppController;
import com.task.omnify.appomnify.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;
import static com.task.omnify.appomnify.Utils.Constants.BY;
import static com.task.omnify.appomnify.Utils.Constants.DELETED;
import static com.task.omnify.appomnify.Utils.Constants.DESCENDANTS;
import static com.task.omnify.appomnify.Utils.Constants.ID;
import static com.task.omnify.appomnify.Utils.Constants.KIDS;
import static com.task.omnify.appomnify.Utils.Constants.PARENT;
import static com.task.omnify.appomnify.Utils.Constants.SCORE;
import static com.task.omnify.appomnify.Utils.Constants.TEXT;
import static com.task.omnify.appomnify.Utils.Constants.TIME;
import static com.task.omnify.appomnify.Utils.Constants.TITLE;
import static com.task.omnify.appomnify.Utils.Constants.TYPE;
import static com.task.omnify.appomnify.Utils.Constants.URL;


public class RequestData {


    public static void requestArticle(String id, final ArticleResponseListener listener){
            String tag_json_obj = "json_article_req"+id;
            String url=Constants.url+id+".json";
                      JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            Article article= new Article();
                            try {
                                article.setBy(response.getString(BY));
                                if(response.has(DESCENDANTS))
                                article.setDescendants(response.getInt(DESCENDANTS));
                                else
                                    article.setDescendants(0);
                                article.setId(response.getInt(ID));

                                List<Integer> listdata = new ArrayList<>();
                                if(response.has(KIDS)) {
                                    JSONArray jArray = response.getJSONArray(KIDS);
                                    article.setDescendants(jArray.length());
                                    article.setKids(jArray.toString());
                                }
                                else {
                                    article.setDescendants(0L);
                                    article.setKids("");
                                }
                                article.setScore(response.getInt(SCORE));
                                article.setTime(response.getLong(TIME));
                                article.setTitle(response.getString(TITLE));
                                article.setType(response.getString(TYPE));
                                if(response.has(URL))
                                article.setUrl(response.getString(URL));
                                else
                                    article.setUrl("");
                                listener.onArticleRetrieved(article);
                            } catch (JSONException e) {
                                listener.onFailure(e.getMessage().toString());
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String msg="Error: " + error.getMessage();
                            listener.onFailure(msg);
                }
            });

// queue Adding request to request
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    public static void requestComment(String id,final CommentResponeListener  listener){
        String tag_json_obj = "json_comment_req"+id;
        String url=Constants.url+id+".json";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        Comment comment= new Comment();
                        try {
                            boolean deleted=response.has(DELETED);
                            if(!deleted){
                                comment=existingCommentInit(response);
                            }
                            else
                                comment=deletedCommentInit(response);
                            listener.onCommentRecieved(comment);
                        } catch (JSONException e) {
                            listener.onFailure(e.getMessage().toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String msg="Error: " + error.getMessage();
                listener.onFailure(msg);
                Log.d(TAG, msg);
            }
        });

// queue Adding request to request
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    public static void requestTopStoriesList(final TopStoriesListListener listener){
        String tag_json_obj = "json_stories_req";

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                Request.Method.GET,
                Constants.url_topStories,
                null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<Long> listdata = new ArrayList<>();
                        if (response != null) {
                            for (int i=0;i<response.length();i++){
                                try {
                                    listdata.add(response.getLong(i));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        listener.onStoriesRecieved(listdata);

                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String msg="Error: " + error.getMessage();
                        listener.onFailure(msg);
                    }
                });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }
            public static Comment deletedCommentInit(JSONObject response)throws JSONException{
                Comment comment = new Comment();
                if(response.has(BY))
                    comment.setBy(response.getString(BY));
                else
                    comment.setBy("");
                comment.setId(response.getString(ID));
                if(response.has(KIDS)){
                    JSONArray jArray = response.getJSONArray(KIDS);
                    comment.setKids(jArray.toString());
                }
                else{
                    comment.setKids("");
                }
                comment.setTime(response.getString(TIME));
                comment.setText(DELETED);
                comment.setType(response.getString(TYPE));
                comment.setParent(response.getString(PARENT));
                return comment;
            }

            public static Comment existingCommentInit(JSONObject response) throws JSONException{
                Comment comment = new Comment();
                if(response.has(BY))
                    comment.setBy(response.getString(BY));
                else
                    comment.setBy("");
                comment.setId(response.getString(ID));
                if(response.has(KIDS)){
                    JSONArray jArray = response.getJSONArray(KIDS);
                    comment.setKids(jArray.toString());
                }
                else{
                    comment.setKids("");
                }
                comment.setTime(response.getString(TIME));
                comment.setText(response.getString(TEXT));
                comment.setType(response.getString(TYPE));
                comment.setParent(response.getString(PARENT));
                return comment;
            }

    }
