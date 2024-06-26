/* Copyright (c) 2018, 2023, Oracle and/or its affiliates.
 * Copyright (C) 1996-2020 Python Software Foundation
 *
 * Licensed under the PYTHON SOFTWARE FOUNDATION LICENSE VERSION 2
 */
/* Frame object interface */

#ifndef Py_FRAMEOBJECT_H
#define Py_FRAMEOBJECT_H
#ifdef __cplusplus
extern "C" {
#endif

#include "pyframe.h"

#ifndef Py_LIMITED_API
#  define Py_CPYTHON_FRAMEOBJECT_H
#  include "cpython/frameobject.h"
#  undef Py_CPYTHON_FRAMEOBJECT_H
#endif

#ifdef __cplusplus
}
#endif
#endif /* !Py_FRAMEOBJECT_H */
