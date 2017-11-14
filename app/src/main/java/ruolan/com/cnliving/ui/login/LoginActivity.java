package ruolan.com.cnliving.ui.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ruolan.com.cnliving.R;
import ruolan.com.cnliving.ui.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {


    private EditText mAccountEdt;
    private EditText mPasswordEdt;
    private Button mLoginBtn;
    private Button mRegisterBtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findAllViews();
        setListeners();
    }

    private void findAllViews() {
        mAccountEdt = (EditText) findViewById(R.id.account);
        mPasswordEdt = (EditText) findViewById(R.id.password);
        mLoginBtn = (Button) findViewById(R.id.login);
        mRegisterBtn = (Button) findViewById(R.id.register);
    }

    private void setListeners() {
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录操作
                login();
            }


        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册的操作
                register();
            }
        });
    }

    private void login() {

        final String accountStr = mAccountEdt.getText().toString();
        String passwordStr = mPasswordEdt.getText().toString();



    }

    private void register() {
        //注册新用户，跳转到注册页面。
        Intent intent = new Intent();
        intent.setClass(this, RegisterActivity.class);
        startActivity(intent);
    }
}
