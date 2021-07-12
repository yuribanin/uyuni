# Copyright (c) 2016-2021 SUSE LLC.
# Licensed under the terms of the MIT license.

require 'net/ssh'
require 'stringio'

# SSH Library
module SSHLib
  # SSH Command
  def sshcmd(command, host: ENV['SERVER'], user: 'root', ignore_err: false)
    # Execute a command on the remote server
    # Not passing :password uses systems keys for auth
    out = StringIO.new
    err = StringIO.new
    Net::SSH.start(host, user, verify_host_key: :never) do |ssh|
      ssh.exec!(command) do |_chan, str, data|
        out << data if str == :stdout
        err << data if str == :stderr
      end
    end
    # smdba print this warning on stderr. Ignore it. It is not an error
    errstring = err.string.gsub(
      'WARNING: Reserved space for the backup is smaller than available disk space. Adjusting.', ''
    )
    errstring.chomp!

    raise(ScriptError, "Execute command failed #{command}: #{err.string}") if !errstring.empty? && !ignore_err

    { stdout: out.string, stderr: err.string }
  end
end
