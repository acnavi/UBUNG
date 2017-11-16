package ubung.co.ubung.Utilidades;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.resource;

/**
 * Created by icmontesdumar on 7/11/17.
 */

public class AsyncForPhotos extends AsyncTask<Void,Void,DrawableRequestBuilder> {

    private boolean yaAcabo;

    private final CircleImageView circleImageView;
    private final CircleImageView circleImageViewCool;
    private final Context context;
    private final StorageReference sr;
    private RequestListener<StorageReference, GlideDrawable> listener;


    public AsyncForPhotos(CircleImageView mientras, CircleImageView laQueEs, Context c,StorageReference stre){
        circleImageView=mientras;
        circleImageViewCool=laQueEs;
        listener= new ListenerParaFoto();
        context=c;
        yaAcabo=false;
        sr = stre;
    }

    @Override
    protected DrawableRequestBuilder doInBackground(Void... params) {
        DrawableRequestBuilder dr= Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(sr).listener(listener);
        while (!yaAcabo);
        return dr;
    }

    @Override
    protected void onPostExecute(DrawableRequestBuilder drawableRequestBuilder) {
        drawableRequestBuilder.dontAnimate().into(circleImageViewCool);
        super.onPostExecute(drawableRequestBuilder);
    }

    public class ListenerParaFoto implements RequestListener<StorageReference, GlideDrawable> {




        @Override
        public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {

            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            circleImageView.setVisibility(View.GONE);
            circleImageViewCool.setVisibility(View.VISIBLE);
            yaAcabo=true;
            return false;
        }
    }
}
