package bfhsoftware.sonidoambiental;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity
{
    /** acalle when when activity CISC when created.
     * @param savedInstanceState */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView text1 = new TextView(this);
        text1.setText("Sonido ambiental esta funcionando");
        setContentView(text1);
    }
}
