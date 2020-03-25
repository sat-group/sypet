using System;

namespace CMU.Commons.Exceptions
{
    /// <summary>Thrown to indicate that an unrecoverable error has occurred.</summary>
    /// <remarks>This exception is usually thrown to indicate that there is a bug in the
    /// code.</remarks>
    public sealed class InternalErrorException : Exception 
    { 
        public InternalErrorException(String message) : base(message) {}
    }
}
