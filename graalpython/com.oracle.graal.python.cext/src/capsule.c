/* Copyright (c) 2021, 2024, Oracle and/or its affiliates.
 * Copyright (C) 1996-2021 Python Software Foundation
 *
 * Licensed under the PYTHON SOFTWARE FOUNDATION LICENSE VERSION 2
 */
/* Wrap void * pointers to be passed between C modules */

#include "Python.h"

#include "capi.h"


PyAPI_FUNC(PyTypeObject*) getPyCapsuleTypeReference() {
	return &PyCapsule_Type;
}

PyAPI_FUNC(void) PyTruffleCapsule_CallDestructor(PyObject* capsule, PyCapsule_Destructor destructor) {
    destructor(capsule);
}

