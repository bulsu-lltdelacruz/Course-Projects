Shader "Custom/PixelatedSpriteLit"
{
    Properties
    {
        [MainTexture] _MainTex ("Color Map", 2D) = "white" {}
        [Toggle(_USE_COLOR)] _USE_COLOR ("Use Color", Float) = 1
        [Normal] _NormalMap ("Normal Map", 2D) = "bump" {}
        _MaskMap ("Mask Map", 2D) = "black" {}

        // Pixelation
        _PixelSize ("Pixel Size", Range(1, 64)) = 8
    }

    SubShader
    {
        Tags
        {
            "Queue" = "Transparent"
            "RenderType" = "Transparent"
            "RenderPipeline" = "UniversalPipeline"
            "UniversalMaterialType" = "Lit"
        }

        // ─────────────────────────────────────────────────
        // PASS 1 – Sprite Lit  (replaces Universal2D pass)
        // ─────────────────────────────────────────────────
        Pass
        {
            Name "Sprite Lit"
            Tags { "LightMode" = "Universal2D" }

            ZWrite Off
            Cull Off
            Blend SrcAlpha OneMinusSrcAlpha, One OneMinusSrcAlpha

            HLSLPROGRAM
            #pragma vertex   vert
            #pragma fragment frag

            #pragma multi_compile USE_SHAPE_LIGHT_TYPE_0 __
            #pragma multi_compile USE_SHAPE_LIGHT_TYPE_1 __
            #pragma multi_compile USE_SHAPE_LIGHT_TYPE_2 __
            #pragma multi_compile USE_SHAPE_LIGHT_TYPE_3 __
            #pragma multi_compile _ DEBUG_DISPLAY
            #pragma multi_compile _ _USE_COLOR

            #include "Packages/com.unity.render-pipelines.universal/ShaderLibrary/Core.hlsl"
            #include "Packages/com.unity.render-pipelines.universal/Shaders/2D/Include/LightingUtility.hlsl"

            TEXTURE2D(_MainTex);  SAMPLER(sampler_MainTex);
            TEXTURE2D(_MaskMap);  SAMPLER(sampler_MaskMap);

            #if USE_SHAPE_LIGHT_TYPE_0
            TEXTURE2D(_ShapeLightTexture0); SAMPLER(sampler_ShapeLightTexture0);
            #endif
            #if USE_SHAPE_LIGHT_TYPE_1
            TEXTURE2D(_ShapeLightTexture1); SAMPLER(sampler_ShapeLightTexture1);
            #endif
            #if USE_SHAPE_LIGHT_TYPE_2
            TEXTURE2D(_ShapeLightTexture2); SAMPLER(sampler_ShapeLightTexture2);
            #endif
            #if USE_SHAPE_LIGHT_TYPE_3
            TEXTURE2D(_ShapeLightTexture3); SAMPLER(sampler_ShapeLightTexture3);
            #endif

            CBUFFER_START(UnityPerMaterial)
                float4 _MainTex_ST;
                float4 _MaskMap_ST;
                float  _PixelSize;
            CBUFFER_END

            // Sprite lit shape-light uniforms are in global CB – Unity provides them.
            #if USE_SHAPE_LIGHT_TYPE_0
            SHAPE_LIGHT(0)
            #endif
            #if USE_SHAPE_LIGHT_TYPE_1
            SHAPE_LIGHT(1)
            #endif
            #if USE_SHAPE_LIGHT_TYPE_2
            SHAPE_LIGHT(2)
            #endif
            #if USE_SHAPE_LIGHT_TYPE_3
            SHAPE_LIGHT(3)
            #endif

            struct Attributes
            {
                float3 positionOS : POSITION;
                float4 color      : COLOR;
                float2 uv         : TEXCOORD0;
                UNITY_VERTEX_INPUT_INSTANCE_ID
            };

            struct Varyings
            {
                float4 positionCS    : SV_POSITION;
                float4 color         : COLOR;
                float2 uv            : TEXCOORD0;
                float2 lightingUV    : TEXCOORD1;
                UNITY_VERTEX_OUTPUT_STEREO
            };

            // ── Pixelation helper ──────────────────────────────
            float2 PixelateUV(float2 uv, float4 st)
            {
                // Work out texel dimensions from the texture's ST tiling
                // _PixelSize = how many screen-pixels per "pixel-art pixel"
                // We snap the UV to a grid of _PixelSize-sized blocks.
                float2 texSize = float2(
                    _ScreenParams.x / _PixelSize,
                    _ScreenParams.y / _PixelSize
                );
                // remap uv into [0,1] ignoring tiling, snap, then remap back
                float2 rawUV = (uv - st.zw) / st.xy;   // undo tiling/offset
                rawUV = floor(rawUV * texSize) / texSize;
                return rawUV * st.xy + st.zw;           // reapply tiling/offset
            }

            Varyings vert(Attributes IN)
            {
                Varyings OUT;
                UNITY_SETUP_INSTANCE_ID(IN);
                UNITY_INITIALIZE_VERTEX_OUTPUT_STEREO(OUT);

                OUT.positionCS = TransformObjectToHClip(IN.positionOS);
                OUT.uv         = IN.uv;
                OUT.color      = IN.color;

                float4 clipPos = OUT.positionCS;
                OUT.lightingUV = clipPos.xy / clipPos.w * 0.5 + 0.5;
                #if UNITY_UV_STARTS_AT_TOP
                OUT.lightingUV.y = 1.0 - OUT.lightingUV.y;
                #endif

                return OUT;
            }

            half4 frag(Varyings IN) : SV_Target
            {
                // ── Pixelated main texture sample ──────────────
                float2 snappedUV = PixelateUV(
                    TRANSFORM_TEX(IN.uv, _MainTex),
                    _MainTex_ST
                );
                half4 mainTex = SAMPLE_TEXTURE2D(_MainTex, sampler_MainTex, snappedUV);

                #if _USE_COLOR
                mainTex *= IN.color;
                #endif

                clip(mainTex.a - 0.001);

                // ── Pixelated mask map sample ───────────────────
                float2 snappedMaskUV = PixelateUV(
                    TRANSFORM_TEX(IN.uv, _MaskMap),
                    _MaskMap_ST
                );
                half4 mask = SAMPLE_TEXTURE2D(_MaskMap, sampler_MaskMap, snappedMaskUV);

                // ── 2D Lighting accumulation ────────────────────
                half4 lightColor = half4(0, 0, 0, 1);

                #if USE_SHAPE_LIGHT_TYPE_0
                {
                    half4 l = SAMPLE_TEXTURE2D(_ShapeLightTexture0, sampler_ShapeLightTexture0, IN.lightingUV);
                    lightColor += CombineColorLayers(mainTex, mask, l,
                        _ShapeLightBlendFactors0, _ShapeLightMaskFilter0, _ShapeLightInvertedFilter0);
                }
                #endif
                #if USE_SHAPE_LIGHT_TYPE_1
                {
                    half4 l = SAMPLE_TEXTURE2D(_ShapeLightTexture1, sampler_ShapeLightTexture1, IN.lightingUV);
                    lightColor += CombineColorLayers(mainTex, mask, l,
                        _ShapeLightBlendFactors1, _ShapeLightMaskFilter1, _ShapeLightInvertedFilter1);
                }
                #endif
                #if USE_SHAPE_LIGHT_TYPE_2
                {
                    half4 l = SAMPLE_TEXTURE2D(_ShapeLightTexture2, sampler_ShapeLightTexture2, IN.lightingUV);
                    lightColor += CombineColorLayers(mainTex, mask, l,
                        _ShapeLightBlendFactors2, _ShapeLightMaskFilter2, _ShapeLightInvertedFilter2);
                }
                #endif
                #if USE_SHAPE_LIGHT_TYPE_3
                {
                    half4 l = SAMPLE_TEXTURE2D(_ShapeLightTexture3, sampler_ShapeLightTexture3, IN.lightingUV);
                    lightColor += CombineColorLayers(mainTex, mask, l,
                        _ShapeLightBlendFactors3, _ShapeLightMaskFilter3, _ShapeLightInvertedFilter3);
                }
                #endif

                // If no lights are active, fall back to the unlit sprite color
                #if !USE_SHAPE_LIGHT_TYPE_0 && !USE_SHAPE_LIGHT_TYPE_1 && !USE_SHAPE_LIGHT_TYPE_2 && !USE_SHAPE_LIGHT_TYPE_3
                return max(0, mainTex);
                #endif

                return max(0, half4(lightColor.rgb * _HDREmulationScale, mainTex.a));
            }
            ENDHLSL
        }

        // ─────────────────────────────────────────────────
        // PASS 2 – Sprite Normal
        // ─────────────────────────────────────────────────
        Pass
        {
            Name "Sprite Normal"
            Tags { "LightMode" = "NormalsRendering" }

            ZWrite Off
            Cull Off
            Blend SrcAlpha OneMinusSrcAlpha, One OneMinusSrcAlpha

            HLSLPROGRAM
            #pragma vertex   vertNormal
            #pragma fragment fragNormal

            #pragma multi_compile _ _USE_COLOR

            #include "Packages/com.unity.render-pipelines.universal/ShaderLibrary/Core.hlsl"
            #include "Packages/com.unity.render-pipelines.universal/Shaders/2D/Include/NormalsRenderingShared.hlsl"

            TEXTURE2D(_NormalMap); SAMPLER(sampler_NormalMap);
            TEXTURE2D(_MainTex);   SAMPLER(sampler_MainTex);

            CBUFFER_START(UnityPerMaterial)
                float4 _NormalMap_ST;
                float4 _MainTex_ST;
                float  _PixelSize;
            CBUFFER_END

            struct Attributes
            {
                float3 positionOS : POSITION;
                float4 color      : COLOR;
                float2 uv         : TEXCOORD0;
                float4 tangentOS  : TANGENT;
                float3 normalOS   : NORMAL;
            };

            struct Varyings
            {
                float4 positionCS : SV_POSITION;
                float4 color      : COLOR;
                float2 uv         : TEXCOORD0;
                float3 normalWS   : TEXCOORD1;
                float3 tangentWS  : TEXCOORD2;
                float3 bitangentWS: TEXCOORD3;
            };

            float2 PixelateUV(float2 uv, float4 st)
            {
                float2 texSize = float2(_ScreenParams.x / _PixelSize, _ScreenParams.y / _PixelSize);
                float2 rawUV = (uv - st.zw) / st.xy;
                rawUV = floor(rawUV * texSize) / texSize;
                return rawUV * st.xy + st.zw;
            }

            Varyings vertNormal(Attributes IN)
            {
                Varyings OUT;
                OUT.positionCS   = TransformObjectToHClip(IN.positionOS);
                OUT.uv           = IN.uv;
                OUT.color        = IN.color;
                OUT.normalWS     = TransformObjectToWorldNormal(IN.normalOS);
                float3 tangentWS = TransformObjectToWorldDir(IN.tangentOS.xyz);
                OUT.tangentWS    = normalize(tangentWS);
                OUT.bitangentWS  = cross(OUT.normalWS, OUT.tangentWS) * IN.tangentOS.w
                                   * unity_WorldTransformParams.w;
                return OUT;
            }

            half4 fragNormal(Varyings IN) : SV_Target
            {
                float2 snappedNormalUV = PixelateUV(
                    TRANSFORM_TEX(IN.uv, _NormalMap), _NormalMap_ST);
                half4 normalSample = SAMPLE_TEXTURE2D(_NormalMap, sampler_NormalMap, snappedNormalUV);

                half3 normalTS;
                normalTS.xy = normalSample.rg * 2.0 - 1.0;
                normalTS.x *= normalSample.a;
                normalTS.z  = sqrt(max(0, 1.0 - dot(normalTS.xy, normalTS.xy)));

                half3 normalWS = normalize(
                    normalTS.x * IN.tangentWS +
                    normalTS.y * IN.bitangentWS +
                    normalTS.z * IN.normalWS
                );

                float2 snappedMainUV = PixelateUV(
                    TRANSFORM_TEX(IN.uv, _MainTex), _MainTex_ST);
                half4 mainTex = SAMPLE_TEXTURE2D(_MainTex, sampler_MainTex, snappedMainUV);

                #if _USE_COLOR
                mainTex *= IN.color;
                #endif

                return half4(normalWS * 0.5 + 0.5, mainTex.a);
            }
            ENDHLSL
        }

        // ─────────────────────────────────────────────────
        // PASS 3 – Sprite Forward (UniversalForward)
        // ─────────────────────────────────────────────────
        Pass
        {
            Name "Sprite Forward"
            Tags { "LightMode" = "UniversalForward" }

            ZWrite Off
            Cull Off
            Blend SrcAlpha OneMinusSrcAlpha, One OneMinusSrcAlpha

            HLSLPROGRAM
            #pragma vertex   vertFwd
            #pragma fragment fragFwd

            #pragma multi_compile _ _USE_COLOR
            #pragma multi_compile _ DEBUG_DISPLAY

            #include "Packages/com.unity.render-pipelines.universal/ShaderLibrary/Core.hlsl"

            TEXTURE2D(_MainTex); SAMPLER(sampler_MainTex);

            CBUFFER_START(UnityPerMaterial)
                float4 _MainTex_ST;
                float  _PixelSize;
            CBUFFER_END

            struct Attributes
            {
                float3 positionOS : POSITION;
                float4 color      : COLOR;
                float2 uv         : TEXCOORD0;
            };

            struct Varyings
            {
                float4 positionCS : SV_POSITION;
                float4 color      : COLOR;
                float2 uv         : TEXCOORD0;
            };

            float2 PixelateUV(float2 uv, float4 st)
            {
                float2 texSize = float2(_ScreenParams.x / _PixelSize, _ScreenParams.y / _PixelSize);
                float2 rawUV = (uv - st.zw) / st.xy;
                rawUV = floor(rawUV * texSize) / texSize;
                return rawUV * st.xy + st.zw;
            }

            Varyings vertFwd(Attributes IN)
            {
                Varyings OUT;
                OUT.positionCS = TransformObjectToHClip(IN.positionOS);
                OUT.uv         = IN.uv;
                OUT.color      = IN.color;
                return OUT;
            }

            half4 fragFwd(Varyings IN) : SV_Target
            {
                float2 snappedUV = PixelateUV(
                    TRANSFORM_TEX(IN.uv, _MainTex), _MainTex_ST);
                half4 col = SAMPLE_TEXTURE2D(_MainTex, sampler_MainTex, snappedUV);

                #if _USE_COLOR
                col *= IN.color;
                #endif

                return col;
            }
            ENDHLSL
        }
    }

    CustomEditor "UnityEditor.ShaderGraph.GenericShaderGraphMaterialGUI"
    Fallback "Hidden/Shader Graph/FallbackError"
}
