package com.bitbang.first_sdk.utils

enum class ServiceStepTypes(val value: Int)
{
    OcrCardFrontSide(1) ,
    OcrCardBackSide(2) ,
    OcrCardVideo(3) ,

    FaceRecognition(10);
}