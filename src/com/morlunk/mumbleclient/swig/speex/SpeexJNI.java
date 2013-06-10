/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.9
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.morlunk.mumbleclient.swig.speex;

public class SpeexJNI {
  public final static native long intToVoidPointer(int[] jarg1);
  public final static native int __EMX___get();
  public final static native void JitterBufferPacket_data_set(long jarg1, JitterBufferPacket jarg1_, byte[] jarg2);
  public final static native byte[] JitterBufferPacket_data_get(long jarg1, JitterBufferPacket jarg1_);
  public final static native void JitterBufferPacket_len_set(long jarg1, JitterBufferPacket jarg1_, long jarg2);
  public final static native long JitterBufferPacket_len_get(long jarg1, JitterBufferPacket jarg1_);
  public final static native void JitterBufferPacket_timestamp_set(long jarg1, JitterBufferPacket jarg1_, long jarg2);
  public final static native long JitterBufferPacket_timestamp_get(long jarg1, JitterBufferPacket jarg1_);
  public final static native void JitterBufferPacket_span_set(long jarg1, JitterBufferPacket jarg1_, long jarg2);
  public final static native long JitterBufferPacket_span_get(long jarg1, JitterBufferPacket jarg1_);
  public final static native void JitterBufferPacket_sequence_set(long jarg1, JitterBufferPacket jarg1_, int jarg2);
  public final static native int JitterBufferPacket_sequence_get(long jarg1, JitterBufferPacket jarg1_);
  public final static native void JitterBufferPacket_user_data_set(long jarg1, JitterBufferPacket jarg1_, long jarg2);
  public final static native long JitterBufferPacket_user_data_get(long jarg1, JitterBufferPacket jarg1_);
  public final static native long new_JitterBufferPacket();
  public final static native void delete_JitterBufferPacket(long jarg1);
  public final static native int JITTER_BUFFER_OK_get();
  public final static native int JITTER_BUFFER_MISSING_get();
  public final static native int JITTER_BUFFER_INSERTION_get();
  public final static native int JITTER_BUFFER_INTERNAL_ERROR_get();
  public final static native int JITTER_BUFFER_BAD_ARGUMENT_get();
  public final static native int JITTER_BUFFER_SET_MARGIN_get();
  public final static native int JITTER_BUFFER_GET_MARGIN_get();
  public final static native int JITTER_BUFFER_GET_AVAILABLE_COUNT_get();
  public final static native int JITTER_BUFFER_GET_AVALIABLE_COUNT_get();
  public final static native int JITTER_BUFFER_SET_DESTROY_CALLBACK_get();
  public final static native int JITTER_BUFFER_GET_DESTROY_CALLBACK_get();
  public final static native int JITTER_BUFFER_SET_DELAY_STEP_get();
  public final static native int JITTER_BUFFER_GET_DELAY_STEP_get();
  public final static native int JITTER_BUFFER_SET_CONCEALMENT_SIZE_get();
  public final static native int JITTER_BUFFER_GET_CONCEALMENT_SIZE_get();
  public final static native int JITTER_BUFFER_SET_MAX_LATE_RATE_get();
  public final static native int JITTER_BUFFER_GET_MAX_LATE_RATE_get();
  public final static native int JITTER_BUFFER_SET_LATE_COST_get();
  public final static native int JITTER_BUFFER_GET_LATE_COST_get();
  public final static native long jitter_buffer_init(int jarg1);
  public final static native void jitter_buffer_reset(long jarg1);
  public final static native void jitter_buffer_destroy(long jarg1);
  public final static native void jitter_buffer_put(long jarg1, long jarg2, JitterBufferPacket jarg2_);
  public final static native int jitter_buffer_get(long jarg1, long jarg2, JitterBufferPacket jarg2_, int jarg3, int[] jarg4);
  public final static native int jitter_buffer_get_another(long jarg1, long jarg2, JitterBufferPacket jarg2_);
  public final static native int jitter_buffer_get_pointer_timestamp(long jarg1);
  public final static native void jitter_buffer_tick(long jarg1);
  public final static native void jitter_buffer_remaining_span(long jarg1, long jarg2);
  public final static native int jitter_buffer_ctl(long jarg1, int jarg2, long jarg3);
  public final static native int jitter_buffer_update_delay(long jarg1, long jarg2, JitterBufferPacket jarg2_, int[] jarg3);
  public final static native int SPEEX_ECHO_GET_FRAME_SIZE_get();
  public final static native int SPEEX_ECHO_SET_SAMPLING_RATE_get();
  public final static native int SPEEX_ECHO_GET_SAMPLING_RATE_get();
  public final static native int SPEEX_ECHO_GET_IMPULSE_RESPONSE_SIZE_get();
  public final static native int SPEEX_ECHO_GET_IMPULSE_RESPONSE_get();
  public final static native long speex_echo_state_init(int jarg1, int jarg2);
  public final static native long speex_echo_state_init_mc(int jarg1, int jarg2, int jarg3, int jarg4);
  public final static native void speex_echo_state_destroy(long jarg1);
  public final static native void speex_echo_cancellation(long jarg1, short[] jarg2, short[] jarg3, short[] jarg4);
  public final static native void speex_echo_cancel(long jarg1, short[] jarg2, short[] jarg3, short[] jarg4, int[] jarg5);
  public final static native void speex_echo_capture(long jarg1, short[] jarg2, short[] jarg3);
  public final static native void speex_echo_playback(long jarg1, short[] jarg2);
  public final static native void speex_echo_state_reset(long jarg1);
  public final static native int speex_echo_ctl(long jarg1, int jarg2, long jarg3);
  public final static native long speex_decorrelate_new(int jarg1, int jarg2, int jarg3);
  public final static native void speex_decorrelate(long jarg1, short[] jarg2, short[] jarg3, int jarg4);
  public final static native void speex_decorrelate_destroy(long jarg1);
  public final static native int SPEEX_RESAMPLER_QUALITY_MAX_get();
  public final static native int SPEEX_RESAMPLER_QUALITY_MIN_get();
  public final static native int SPEEX_RESAMPLER_QUALITY_DEFAULT_get();
  public final static native int SPEEX_RESAMPLER_QUALITY_VOIP_get();
  public final static native int SPEEX_RESAMPLER_QUALITY_DESKTOP_get();
  public final static native int RESAMPLER_ERR_SUCCESS_get();
  public final static native int RESAMPLER_ERR_ALLOC_FAILED_get();
  public final static native int RESAMPLER_ERR_BAD_STATE_get();
  public final static native int RESAMPLER_ERR_INVALID_ARG_get();
  public final static native int RESAMPLER_ERR_PTR_OVERLAP_get();
  public final static native int RESAMPLER_ERR_MAX_ERROR_get();
  public final static native long speex_resampler_init(long jarg1, long jarg2, long jarg3, int jarg4, int[] jarg5);
  public final static native long speex_resampler_init_frac(long jarg1, long jarg2, long jarg3, long jarg4, long jarg5, int jarg6, int[] jarg7);
  public final static native void speex_resampler_destroy(long jarg1);
  public final static native int speex_resampler_process_float(long jarg1, long jarg2, float[] jarg3, int[] jarg4, float[] jarg5, int[] jarg6);
  public final static native int speex_resampler_process_int(long jarg1, long jarg2, short[] jarg3, int[] jarg4, short[] jarg5, int[] jarg6);
  public final static native int speex_resampler_process_interleaved_float(long jarg1, float[] jarg2, int[] jarg3, float[] jarg4, int[] jarg5);
  public final static native int speex_resampler_process_interleaved_int(long jarg1, short[] jarg2, int[] jarg3, short[] jarg4, int[] jarg5);
  public final static native int speex_resampler_set_rate(long jarg1, long jarg2, long jarg3);
  public final static native void speex_resampler_get_rate(long jarg1, int[] jarg2, int[] jarg3);
  public final static native int speex_resampler_set_rate_frac(long jarg1, long jarg2, long jarg3, long jarg4, long jarg5);
  public final static native void speex_resampler_get_ratio(long jarg1, int[] jarg2, int[] jarg3);
  public final static native int speex_resampler_set_quality(long jarg1, int jarg2);
  public final static native void speex_resampler_get_quality(long jarg1, int[] jarg2);
  public final static native void speex_resampler_set_input_stride(long jarg1, long jarg2);
  public final static native void speex_resampler_get_input_stride(long jarg1, int[] jarg2);
  public final static native void speex_resampler_set_output_stride(long jarg1, long jarg2);
  public final static native void speex_resampler_get_output_stride(long jarg1, int[] jarg2);
  public final static native int speex_resampler_get_input_latency(long jarg1);
  public final static native int speex_resampler_get_output_latency(long jarg1);
  public final static native int speex_resampler_skip_zeros(long jarg1);
  public final static native int speex_resampler_reset_mem(long jarg1);
  public final static native byte[] speex_resampler_strerror(int jarg1);
}
