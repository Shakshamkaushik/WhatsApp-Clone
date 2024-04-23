package com.example.whatsappclone.utils

import android.content.Context
import com.example.whatsappclone.constants.SharedPrefrencesConstant

class SharedPrefrencesUtil {


  companion object {
      fun getStringPrefrences(context: Context,key : String) : String? {
          val sharedPrefrences = context.getSharedPreferences(
              SharedPrefrencesConstant.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
          )
          return sharedPrefrences.getString(key,null)
      }

      fun getIntegerPrefrences(context: Context,key : String) : Int? {
          val sharedPrefrences = context.getSharedPreferences(
              SharedPrefrencesConstant.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
          )
          return sharedPrefrences.getInt(key,0)
      }

      fun getBooleanPrefrences(context: Context,key : String) : Boolean? {
          val sharedPrefrences = context.getSharedPreferences(
              SharedPrefrencesConstant.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
          )
          return sharedPrefrences.getBoolean(key,false)
      }


      fun setStringPrefrences(context: Context, key: String, value: String) {
          val sharedPrefrences = context.getSharedPreferences(
              SharedPrefrencesConstant.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
          )
          val sharedPreferencesEditor = sharedPrefrences.edit()
          sharedPreferencesEditor.putString(key,value)
          sharedPreferencesEditor.apply()
      }

      fun setBooleanPrefrences(context: Context, key: String, value: Boolean) {
          val sharedPrefrences = context.getSharedPreferences(
              SharedPrefrencesConstant.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
          )
          val sharedPreferencesEditor = sharedPrefrences.edit()
          sharedPreferencesEditor.putBoolean(key,value)
          sharedPreferencesEditor.apply()
      }
      fun setIntegerPrefrences(context: Context, key: String, value: Int) {
          val sharedPrefrences = context.getSharedPreferences(
              SharedPrefrencesConstant.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
          )
          val sharedPreferencesEditor = sharedPrefrences.edit()
          sharedPreferencesEditor.putInt(key,value)
          sharedPreferencesEditor.apply()
      }

      fun clearAllSharedPrefrences(context: Context) {
          val sharedPreference = context.getSharedPreferences(
              SharedPrefrencesConstant.SHARED_PREFERENCE_NAME,
              Context.MODE_PRIVATE
          )

          val editor = sharedPreference.edit()
          editor.clear()
          editor.apply()
      }
  }
}