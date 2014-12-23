package josias.jsonparsing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        init();
    }

    private void init(){
        Button jacksonParsingBTN = (Button) findViewById(R.id.jacksonParsingBTN);
        Button normalJSONParsing = (Button) findViewById(R.id.normalJSONParsing);

        jacksonParsingBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JacksonParsingActivity.class);
                startActivity(intent);
            }
        });

        normalJSONParsing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NormalJSONParsing.class);
                startActivity(intent);
            }
        });
    }
}
