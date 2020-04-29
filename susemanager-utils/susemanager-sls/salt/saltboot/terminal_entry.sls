# Part of the Saltboot process
# generate pxe and grub configuration for one terminal

{%- set pxeroot = pillar.get('pxe', {}).get('pxe_root_directory', '/srv/saltboot') -%}
{%- set pxepath_cfg = pxeroot + '/boot/pxelinux.cfg' -%}

{% for nic, mac in pillar['terminal_hwaddr_interfaces'].items() if nic != 'lo' %}
{{ pxepath_cfg + '/01-' + mac.lower().replace(':', '-') }}:
  file.managed:
    - source: salt://pxe/files/pxecfg.template
    - user: root
    - group: root
    - mode: 644
    - template: jinja

{{ pxepath_cfg + '/01:' + mac.lower() }}.grub2.cfg:
  file.managed:
    - source: salt://pxe/files/pxecfg.grub2.template
    - user: root
    - group: root
    - mode: 644
    - template: jinja
{% endfor %}
