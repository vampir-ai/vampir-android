package com.sub6resources.vampir

import com.sub6resources.utilities.*

class LegalActivity: BaseLegalActivity() {
    override val legal = legalActivity {
        group {
            copyright = "App Copyright (c) 2018 Vampïr"
        }
        group {
            title = "Terms and Privacy"
            terms {
                title = "Terms of Service"
                lastUpdatedText = ""
                text = "" //TODO Update
            }

            privacy {
                title = "Privacy Policy"
                lastUpdatedText = ""
                text = "" //TODO Update
            }
        }

        group {
            title = "3rd Party Libraries"

            acknowledgement {
                title = "Android Support Libraries"
                copyright = "Copyright (C) 2018 The Android Open Source Project"
                license = License.APACHE(copyright)
            }

            acknowledgement {
                title = "Firebase JobDispatcher"
                license = License.APACHE_GENERIC
            }

            acknowledgement {
                title = "Koin"
                copyright = "Copyright 2017 Arnaud GIULIANI"
                license = License.APACHE(copyright)
            }

            acknowledgement {
                title = "kotlinx.coroutines"
                copyright = "Copyright 2016-2017 JetBrains s.r.o."
                license = License.APACHE(copyright)
            }

            acknowledgement {
                title = "Material Dialogs"
                copyright = "Copyright (c) 2014-2016 Aidan Michael Follestad"
                license = License.MIT(copyright)
            }

            acknowledgement {
                title = "MPAndroidChart"
                copyright = "Copyright 2018 Philipp Jahoda"
                license = License.APACHE(copyright)
            }

            acknowledgement {
                title = "OkHttp"
                copyright = "Copyright 2016 Square, Inc."
                license = License.APACHE(copyright)
            }

            acknowledgement {
                title = "Picasso"
                copyright = "Copyright 2013 Square, Inc."
                license = License.APACHE(copyright)
            }

            acknowledgement {
                title = "Retrofit"
                copyright = "Copyright 2013 Square, Inc."
                license = License.APACHE(copyright)
            }

            acknowledgement {
                title = "RxAndroid"
                copyright = "Copyright 2015, The RxAndroid authors"
                license = License.APACHE(copyright)
            }

            acknowledgement {
                title = "RxJava"
                copyright = "Copyright 2013 Netflix, Inc."
                license = License.APACHE(copyright)
            }

            acknowledgement {
                title = "Utilities"
                copyright = "Copyright (c) 2018 Matthew Whitaker"
                license = License.MIT(copyright)
            }
        }
    }
}