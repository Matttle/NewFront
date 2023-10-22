package com.example.myapplication.ui.agreement;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.example.myapplication.R;

public class AgreementActivity extends AppCompatActivity {
    private String agreement = "<b>NAVIGATIONAL APP LIABILITY DISCLAIMER AGREEMENT</b><br/><br/>\n" +
            "    This Navigational App Liability Disclaimer Agreement (\"Agreement\") is entered into on [Date] (\"Effective Date\") by and between:<br/><br/>\n" +
            "    <b>New Front</b>, a company registered under the laws of [Your Jurisdiction], with its principal place of business at [Your Address] (\"Company\"), and<br/><br/>\n" +
            "    <b>[Users Full Name]</b>, residing at [User Address] (\"User\").<br/><br/>\n" +
            "    (collectively referred to as \"Parties\").<br/><br/>\n" +
            "    <b>RECITALS:</b><br/><br/>\n" +
            "    WHEREAS, New Front has developed a navigational mobile application known as \"Pay Per Mile\" (\"App\") that provides navigational and route information to Users;<br/><br/>\n" +
            "    WHEREAS, the User desires to use the App for navigational purposes;<br/><br/>\n" +
            "    NOW, THEREFORE, in consideration of the premises and mutual covenants contained herein, the Parties agree as follows:<br/><br/>\n" +
            "    <b>1. Disclaimer of Liability:</b><br/><br/>\n" +
            "    1.1 The User acknowledges and agrees that New Front, its officers, directors, employees, agents, and any third parties involved in the creation, maintenance, or operation of the App shall not be liable for any accidents, injuries, damages, losses, or any other claims arising out of or in connection with the use of the App.<br/><br/>\n" +
            "    1.2 New Front makes no representations or warranties, either express or implied, regarding the accuracy, completeness, reliability, suitability, or availability of the App for any particular purpose. The User understands that the App is provided on an \"as is\" and \"as available\" basis.<br/><br/>\n" +
            "    <b>2. User Responsibility:</b><br/><br/>\n" +
            "    2.1 The User is solely responsible for using the App safely and in compliance with all applicable traffic laws and regulations. New Front shall not be responsible for any User's actions or violations of laws while using the App.<br/><br/>\n" +
            "    2.2 The User acknowledges that the App is for informational and navigational purposes only, and it is the User's responsibility to exercise caution, judgment, and common sense when using the App.<br/><br/>\n" +
            "    <b>3. Indemnification:</b><br/><br/>\n" +
            "    3.1 The User agrees to indemnify and hold harmless New Front, its officers, directors, employees, and agents from and against any claims, damages, liabilities, losses, and expenses (including attorney's fees) arising out of or in connection with the User's use of the App.<br/><br/>\n" +
            "    <b>4. Assumption of Risk:</b><br/><br/>\n" +
            "    4.1 The User expressly acknowledges that the use of the App involves inherent risks, and the User assumes all risks and responsibilities associated with such use.<br/><br/>\n" +
            "    <b>5. Governing Law:</b><br/><br/>\n" +
            "    5.1 This Agreement shall be governed by and construed in accordance with the laws of [Your Jurisdiction].<br/><br/>\n" +
            "    <b>6. Entire Agreement:</b><br/><br/>\n" +
            "    6.1 This Agreement constitutes the entire agreement between the Parties concerning the subject matter hereof and supersedes all prior understandings, agreements, or representations, whether written or oral.<br/><br/>\n" +
            "    IN WITNESS WHEREOF, the Parties hereto have executed this Navigational App Liability Disclaimer Agreement as of the Effective Date.<br/><br/>\n" +
            "    <b>Company</b>: New Front [Authorized Signatory's Name] [Date]\n" +
            "    <b>User</b>: [User's Full Name] [Electronic Signature] [Date]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        TextView textView = findViewById(R.id.txt_agreement);
        textView.setText(Html.fromHtml(agreement));


    }
}