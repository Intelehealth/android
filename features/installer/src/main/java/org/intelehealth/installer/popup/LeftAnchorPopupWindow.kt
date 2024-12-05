package org.intelehealth.installer.popup

import android.content.Context
import android.view.View

/**
 * Created by Vaghela Mithun R. on 18-11-2024 - 11:18.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class LeftAnchorPopupWindow(
    context: Context, anchorView: View, listener: OnDownloadCompleteListener
) : DownloadPopupWindow(context, anchorView, listener) {}