package com.lsh.fillette.AppManaging;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.lsh.fillette.R;

/**
 * Created by jjw on 2018-06-04.
 * Show open source licenses.
 */

public class License extends AppCompatActivity{

    private String[] licenses = {
            "The following software may be included in this product:\n" +
                    "Android Design Support Library 27.0.0,\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product:\n" +
                    "Android ConstraintLayout 1.0.2,\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product:\n" +
                    "Android Support Library V4 27.1.0,\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product:\n" +
                    "FirebaseUI-storage-Android,\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product:\n" +
                    "Android AppCompat Library V7 27.0.2,\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product:\n" +
                    "Android Support CardView V7,\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product:\n" +
                    "Android Support RecyclerView V7,\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product:\n" +
                    "Volley 1.1.0,\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product:\n" +
                    "KAKAO.sdk,\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product:\n" +
                    "Firebase-database,\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product:\n" +
                    "Firebase-auth,\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product:\n" +
                    "Firebase-client,\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product:\n" +
                    "gson 2.5,\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product:\n" +
                    "Joda Time Android Library 2.9.0,\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product:\n" +
                    "CircleImageView 2.2.0,\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product:\n" +
                    "Android Volley Library,\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product: glide 4.7.1.\n" +
                    "This software contains the following license and notice below:\n" +
                    "\n" +
                    "License for everything not in third_party and not otherwise marked:\n" +
                    "\n" +
                    "Copyright 2014 Google, Inc. All rights reserved.\n" +
                    "\n" +
                    "Redistribution and use in source and binary forms, with or without modification, are\n" +
                    "permitted provided that the following conditions are met:\n" +
                    "\n" +
                    "   1. Redistributions of source code must retain the above copyright notice, this list of\n" +
                    "         conditions and the following disclaimer.\n" +
                    "\n" +
                    "   2. Redistributions in binary form must reproduce the above copyright notice, this list\n" +
                    "         of conditions and the following disclaimer in the documentation and/or other materials\n" +
                    "         provided with the distribution.\n" +
                    "\n" +
                    "THIS SOFTWARE IS PROVIDED BY GOOGLE, INC. ``AS IS'' AND ANY EXPRESS OR IMPLIED\n" +
                    "WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND\n" +
                    "FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GOOGLE, INC. OR\n" +
                    "CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR\n" +
                    "CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR\n" +
                    "SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON\n" +
                    "ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING\n" +
                    "NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF\n" +
                    "ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.\n" +
                    "\n" +
                    "The views and conclusions contained in the software and documentation are those of the\n" +
                    "authors and should not be interpreted as representing official policies, either expressed\n" +
                    "or implied, of Google, Inc.\n" +
                    "---------------------------------------------------------------------------------------------\n" +
                    "License for third_party/disklrucache:\n" +
                    "\n" +
                    "Copyright 2012 Jake Wharton\n" +
                    "Copyright 2011 The Android Open Source Project\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License.\n" +
                    "---------------------------------------------------------------------------------------------\n" +
                    "License for third_party/gif_decoder:\n" +
                    "\n" +
                    "Copyright (c) 2013 Xcellent Creations, Inc.\n" +
                    "\n" +
                    "Permission is hereby granted, free of charge, to any person obtaining\n" +
                    "a copy of this software and associated documentation files (the\n" +
                    "\"Software\"), to deal in the Software without restriction, including\n" +
                    "without limitation the rights to use, copy, modify, merge, publish,\n" +
                    "distribute, sublicense, and/or sell copies of the Software, and to\n" +
                    "permit persons to whom the Software is furnished to do so, subject to\n" +
                    "the following conditions:\n" +
                    "\n" +
                    "The above copyright notice and this permission notice shall be\n" +
                    "included in all copies or substantial portions of the Software.\n" +
                    "\n" +
                    "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND,\n" +
                    "EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF\n" +
                    "MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND\n" +
                    "NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE\n" +
                    "LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION\n" +
                    "OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION\n" +
                    "WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\n" +
                    "---------------------------------------------------------------------------------------------\n" +
                    "License for third_party/gif_encoder/AnimatedGifEncoder.java and\n" +
                    "third_party/gif_encoder/LZWEncoder.java:\n" +
                    "\n" +
                    "No copyright asserted on the source code of this class. May be used for any\n" +
                    "purpose, however, refer to the Unisys LZW patent for restrictions on use of\n" +
                    "the associated LZWEncoder class. Please forward any corrections to\n" +
                    "kweiner@fmsware.com.\n" +
                    "\n" +
                    "-----------------------------------------------------------------------------\n" +
                    "License for third_party/gif_encoder/NeuQuant.java\n" +
                    "\n" +
                    "Copyright (c) 1994 Anthony Dekker\n" +
                    "\n" +
                    "NEUQUANT Neural-Net quantization algorithm by Anthony Dekker, 1994. See\n" +
                    "\"Kohonen neural networks for optimal colour quantization\" in \"Network:\n" +
                    "Computation in Neural Systems\" Vol. 5 (1994) pp 351-367. for a discussion of\n" +
                    "the algorithm.\n" +
                    "\n" +
                    "Any party obtaining a copy of these files from the author, directly or\n" +
                    "indirectly, is granted, free of charge, a full and unrestricted irrevocable,\n" +
                    "world-wide, paid up, royalty-free, nonexclusive right and license to deal in\n" +
                    "this software and documentation files (the \"Software\"), including without\n" +
                    "limitation the rights to use, copy, modify, merge, publish, distribute,\n" +
                    "sublicense, and/or sell copies of the Software, and to permit persons who\n" +
                    "receive copies from any such party to do so, with the only requirement being\n" +
                    "that this copyright notice remain intact.\n"
            ,
            "The following software may be included in this product: android-autofittextview\n" +
                    "This software contains the following license and notice below:\n" +
                    "\n" +
                    "Copyright 2014 Grantland Chew\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product: Android-SDK-Estimote\n" +
                    "This software contains the following license and notice below:\n" +
                    "\n" +
                    "The MIT License (MIT)\n" +
                    "\n" +
                    "\n" +
                    "Copyright (c) 2013 Estimote, Inc.\n" +
                    "\n" +
                    "\n" +
                    "Permission is hereby granted, free of charge, to any person obtaining a copy of\n" +
                    "this software and associated documentation files (the \"Software\"), to deal in\n" +
                    "the Software without restriction, including without limitation the rights to\n" +
                    "use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of\n" +
                    "the Software, and to permit persons to whom the Software is furnished to do so,\n" +
                    "subject to the following conditions:\n" +
                    "\n" +
                    "\n" +
                    "The above copyright notice and this permission notice shall be included in all\n" +
                    "copies or substantial portions of the Software.\n" +
                    "\n" +
                    "\n" +
                    "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
                    "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS\n" +
                    "FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR\n" +
                    "COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER\n" +
                    "IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN\n" +
                    "CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\n"
            ,
            "The following software may be included in this product: photofilter\n" +
                    "This software contains the following license and notice below:\n" +
                    "\n" +
                    "Copyright 2018 Mukesh Solanki\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License.\n"
            ,
            "The following software may be included in this product: AnimatiedSvgView\n" +
                    "This software contains the following license and notice below:\n" +
                    "\n" +
                    "Copyright (C) 2016, Jared Rummler\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License.\n"
            ,
            "The following software may be included in this product: FloatingActionButton\n" +
                    "This software contains the following license and notice below:\n" +
                    "\n" +
                    "Copyright (C) 2014 Jerzy Chalupski\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "     http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product: ColorSeekBar\n" +
                    "This software contains the following license and notice below:\n" +
                    "\n" +
                    "DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE\n" +
                    "                Version 2, December 2004\n" +
                    "\n" +
                    "Copyright (C) 2004 Leon Fu <rtugeek@gmail.com>\n" +
                    "\n" +
                    "Everyone is permitted to copy and distribute verbatim or modified\n" +
                    "copies of this license document, and changing it is allowed as long\n" +
                    "as the name is changed.\n" +
                    "\n" +
                    "        DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE\n" +
                    "TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION\n" +
                    "\n" +
                    " 0. You just DO WHAT THE FUCK YOU WANT TO."
            ,
            "The following software may be included in this product: Sweet Alert Dialog\n" +
                    "This software contains the following license and notice below:\n" +
                    "\n" +
                    "The MIT License (MIT)\n" +
                    "\n" +
                    "Copyright (c) 2014 Pedant(http://pedant.cn)\n" +
                    "\n" +
                    "Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
                    "of this software and associated documentation files (the \"Software\"), to deal\n" +
                    "in the Software without restriction, including without limitation the rights\n" +
                    "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
                    "copies of the Software, and to permit persons to whom the Software is\n" +
                    "furnished to do so, subject to the following conditions:\n" +
                    "\n" +
                    "The above copyright notice and this permission notice shall be included in all\n" +
                    "copies or substantial portions of the Software.\n" +
                    "\n" + "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
                    "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
                    "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
                    "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
                    "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
                    "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\n" +
                    "SOFTWARE."
            ,
            "The following software may be included in this product: RecyclerRefreshLayout\n" +
                    "This software contains the following license and notice below:\n" +
                    "\n" +
                    "Copyright 2015-2019 dinus\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License."
            ,
            "The following software may be included in this product:\n" +
                    "TedBottomPicker,\n" +
                    "TedPermission.\n" +
                    "This software contains the following license and notice below:\n" +
                    "\n" +
                    "Copyright 2017 Ted Park\n" +
                    "\n" +
                    "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "you may not use this file except in compliance with the License.\n" +
                    "You may obtain a copy of the License at\n" +
                    "\n" +
                    "http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "Unless required by applicable law or agreed to in writing, software\n" +
                    "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "See the License for the specific language governing permissions and\n" +
                    "limitations under the License.```"
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        String licenseSet = "\nTHE FOLLOWING SETS FORTH ATTRIBUTION NOTICES FOR THIRD PARTY SOFTWARE THAT MAY BE CONTAINED IN THIS APPLICATION.";
        for(String s : licenses){
            licenseSet += "\n\n ======== \n\n" + s;
        }

        TextView textView = (TextView)findViewById(R.id.licenses);
        textView.setText(licenseSet);
    }

}